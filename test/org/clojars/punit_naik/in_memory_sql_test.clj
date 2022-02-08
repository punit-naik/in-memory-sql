(ns org.clojars.punit-naik.in-memory-sql-test
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.test :refer [deftest testing is use-fixtures]]
            [org.clojars.punit-naik.in-memory-sql :as imsql])
  (:import [org.sqlite SQLiteException]))

(def db (atom nil))

(defn db-system-fixture
  [f]
  (reset! db (imsql/start
              "CREATE TABLE users (id INT NOT NULL, name TEXT NOT NULL)"
              "CREATE TABLE users2 (id INT, name TEXT)\nCREATE TABLE users3 (id INT NOT NULL, name TEXT)"
              "INSERT INTO users VALUES(1, 'punit1'),(2, 'punit2')"))
  (f)
  (imsql/stop @db)
  (reset! db nil))

(use-fixtures :once db-system-fixture)

(deftest query-tests
  (testing "queries on created tables"
    (is (= (jdbc/query @db ["SELECT COUNT(id) id_count FROM users"]
                       {:row-fn :id_count
                        :result-set-fn first})
           2))
    (is (= (jdbc/query @db ["SELECT * FROM users"])
           [{:id 1 :name "punit1"}
            {:id 2 :name "punit2"}]))
    (is (not (seq (jdbc/query @db ["SELECT * FROM users2"]))))
    (try (jdbc/execute! @db ["INSERT INTO users3 VALUES(1, 'punit1'),(null, 'punit2')"])
         (catch SQLiteException e
           (is (= (.getMessage e)
                  "[SQLITE_CONSTRAINT_NOTNULL]  A NOT NULL constraint failed (NOT NULL constraint failed: users3.id)"))))
    (is (= (jdbc/query @db ["SELECT COUNT(id) id_count FROM users3"]
                       {:row-fn :id_count
                        :result-set-fn first})
           0))))