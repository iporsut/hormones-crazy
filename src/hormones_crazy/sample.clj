(ns hormones-crazy.sample
  (:require [clojure.core.async :refer [go-loop <!]]
            [hormones-crazy.twitter :as twitter]))

(defn write-open-vector [filepath]
  (spit filepath "["))

(defn ensure-vector-close [filepath]
  (.addShutdownHook (Runtime/getRuntime) (Thread. (fn [] (spit filepath "]" :append true)))))

(defn read-creds []
  (read-string (slurp "resources/credentials.edn")))

(defn write-message [source writer]
  (go-loop []
    (writer (<! source))
    (recur)))

(defn tweets-to-file [creds match-string filepath]
  (write-open-vector filepath)
  (ensure-vector-close filepath)
  (letfn [(writer [message] (spit filepath message :append true))]
    (write-message (twitter/fetch creds match-string) writer)))

(defn -main [match-string outpath]
  (tweets-to-file (read-creds) match-string outpath))
