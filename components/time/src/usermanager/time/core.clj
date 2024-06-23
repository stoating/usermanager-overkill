(ns usermanager.time.core)

(println "in ns:" (str *ns*))


(defn- expand-now [x]
  (if (= x :now)
    (java.util.Date.)
    x))


(defn seconds-between [t1 t2]
  (quot (- (inst-ms (expand-now t2)) (inst-ms (expand-now t1))) 1000))


(defn seconds-in [x unit]
  (case unit
    :seconds x
    :minutes (* x 60)
    :hours (* x 60 60)
    :days (* x 60 60 24)
    :weeks (* x 60 60 24 7)))


(defn elapsed? [t1 t2 x unit]
  (<= (seconds-in x unit)
      (seconds-between t1 t2)))