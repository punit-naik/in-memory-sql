(ns org.clojars.punit-naik.in-memory-sql
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.string :as str]))

(defn start
  "Starts up the DB and optionally runs `setup-ddl-statements` defined as string in a vector or a string separated by `\n`
   Returns the `db` object"
  [& setup-ddl-statements]
  (let [db (->> "jdbc:sqlite::memory:"
                (assoc {} :connection-uri)
                jdbc/get-connection
                (assoc {} :connection))]
    (->> setup-ddl-statements
         (mapcat
          (fn [statement]
            (if (str/includes? statement "\n")
              (->> (str/split statement #"\n")
                   (filter seq))
              (list statement))))
         (jdbc/db-do-commands db))
    db))

(defn stop
  [db]
  (-> db :connection .close))
