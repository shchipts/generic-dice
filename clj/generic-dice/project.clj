(defproject org.clojars.shchipts/generic-dice "0.1.0-SNAPSHOT"
  :description "Generic DICE simulations (economic module)"
  :url "https://github.com/shchipts/generic-dice"
  :scm {:name "git"
        :url "https://github.com/shchipts/generic-dice"
        :dir "clj/generic-dice"}
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[clojure-csv/clojure-csv "2.0.2"]
                 [org.clojars.shchipts/dice-simulator "1.0.0"]
                 [org.clojure/clojure "1.10.3"]
                 [org.iiasa/utilities-clj "1.1.0"]]
  :plugins [[lein-codox "0.9.5"]]
  :codox {:output-path "docs"}
  :main ^:skip-aot generic-dice.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
