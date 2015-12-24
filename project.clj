(defproject clojure_applied "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0-RC4"]
                 
                 ;; schema and types
                 [prismatic/schema "1.0.4"]

                 ;; persistence layer

                 [org.clojure/core.async "0.2.374"]

                 ;; testing tools
                 [org.clojure/test.check "0.9.0"]
                 [expectations "2.1.4"]

                 [org.clojure/data.json "0.2.6"]
                 [cheshire "5.5.0"]
                 [medley "0.7.0"]
                 [com.cognitect/transit-clj "0.8.285"]]

  :aot :all)
