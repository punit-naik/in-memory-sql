(defproject org.clojars.punit-naik/in-memory-sql "0.1.0"
  :description "A Clojure library designed to work with an in-memory version of SQL DB for testing purposes."
  :url "https://github.com/punit-naik/in-memory-sql"
  :license {:name "EPL-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [org.xerial/sqlite-jdbc "3.36.0.3"]]
  :repl-options {:init-ns org.clojars.punit-naik.in-memory-sql})
