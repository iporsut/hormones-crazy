(ns hormones-crazy.analyze-test
  (:require [clojure.test :refer :all]
            [hormones-crazy.analyze :refer [group-id-by-minute]]
            [clj-time.coerce :as t]))

(defn string->epoch [string-time]
  (str (t/to-long (t/from-string string-time))))

(deftest test-group-id-by-minute
  (let [data [{:id_str "aaa" :timestamp_ms (string->epoch "2015-11-20T03:20:00.000Z")}
              {:id_str "bbb" :timestamp_ms (string->epoch "2015-11-20T03:20:59.999Z")}
              {:id_str "ccc" :timestamp_ms (string->epoch "2015-11-20T03:22:30.000Z")}]]
    (is (= (group-id-by-minute data)
           {(string->epoch "2015-11-20T03:20:00.000Z") 2
            (string->epoch "2015-11-20T03:22:00.000Z") 1}))))
