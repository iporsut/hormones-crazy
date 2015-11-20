(ns hormones-crazy.twitter
  (:require [twitter-streaming-client.core :as lib]
            [twitter.oauth :as oauth]
            [clojure.core.async :refer [chan >!!]]))

(defn start-background-fetch [{:keys [consumer-key consumer-secret user-access-token user-access-token-secret]} track]
  (let [creds (oauth/make-oauth-creds consumer-key consumer-secret user-access-token user-access-token-secret)
        conn (lib/create-twitter-stream twitter.api.streaming/statuses-filter :oauth-creds creds :params {:track track})]
    (lib/start-twitter-stream conn)
    conn))

(defn start-publish-loop [get-tweets out-chan]
  (future
    (loop []
      (Thread/sleep 10000)
      (doseq [tweet (get-tweets)]
        (>!! out-chan tweet))
      (recur))))

(defn fetch
  ([creds track]
   (let [out-chan (chan)]
     (fetch creds track out-chan) out-chan))
  ([creds track out-chan]
   (let [conn (start-background-fetch creds track)
         get-tweets-fn #(lib/retrieve-queues conn)]
     (start-publish-loop get-tweets-fn out-chan))))
