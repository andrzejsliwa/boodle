(ns boodle.model.aims
  (:require [boodle.services.postgresql :as db]))

(defn select-all
  []
  (db/query ["SELECT * FROM aims"]))

(defn select-by-id
  [id]
  (db/query ["SELECT * FROM aims WHERE id = cast(? as integer)" id]))

(defn select-by-name
  [aim-name]
  (db/query ["SELECT * FROM aims WHERE name = ?" aim-name]))

(defn select-active
  []
  (db/query ["SELECT * FROM aims WHERE achieved = false"]))

(defn select-achieved
  []
  (db/query ["SELECT * FROM aims WHERE achieved = true"]))

(defn select-aims-with-transactions
  []
  (db/query ["SELECT a.id, a.name as aim, a.target, t.amount
              FROM transactions t
              RIGHT JOIN aims a ON a.id = t.id_aim
              WHERE a.achieved = false"]))

(defn insert!
  [aim]
  (let [{:keys [name target]} aim]
    (db/update! ["INSERT INTO aims(name, target, achieved)
                  VALUES (?, cast(? as double precision), false)"
                 name target])))

(defn update!
  [aim]
  (let [{:keys [id name target achieved]} aim]
    (db/update! ["UPDATE aims SET name = ?,
                  target = cast(? as double precision), achieved = ?
                  WHERE id = cast(? as integer)"
                 name target achieved id])))

(defn delete!
  [id]
  (db/delete! ["DELETE FROM transactions WHERE id_aim = cast(? as integer)" id])
  (db/delete! ["DELETE FROM aims WHERE id = cast(? as integer)" id]))
