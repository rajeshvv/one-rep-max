(ns one.repmax.snippets
  "Macros for including HTML snippets in the ClojureScript application
  at compile time."
  (:use [one.templates :only (render construct-html)])
  (:require [net.cgrand.enlive-html :as html]))

(defn- snippet [file id]
  (render (html/select (construct-html (html/html-resource file)) id)))

(def ^{:private true}
  exercises-content-template
  (let [content-with-sample-items (html/select (html/html-resource "exercises.html") [:#content])
        content-with-empty-list (html/at content-with-sample-items [:exercise-list :div :ol] (html/content ""))]
    (render content-with-empty-list)))

(def ^{:private true}
  exercises-list-item-template
  (let [sample-list-item (html/select (html/html-resource "exercises.html") [:#exercise-list :ol [:li html/first-of-type]])
        list-item-without-content (html/at sample-list-item [:.list-item-label] (html/content ""))]
    (render list-item-without-content)))

(def ^{:private true}
  set-history-div-template
  (let [div-with-sample-content (html/select (html/html-resource "new_set.html") [:#recent-sets-by-day])
        empty-div (html/at div-with-sample-content [:#recent-sets-by-day] (html/content ""))]
    (render empty-div)))

(def ^{:private true}
  set-history-list-template
  (let [list-with-sample-items (html/select (html/html-resource "new_set.html") [:.set-history])
      empty-list (html/at list-with-sample-items [:ol] (html/content ""))]
  (render empty-list)))

(def ^{:private true}
  set-history-list-item-template
  (let [sample-list-item (html/select (html/html-resource "new_set.html") [:.set-history :ol [:li html/first-of-type]])]
    (-> sample-list-item
      (html/at [:p.note] (html/substitute ""))        ; TODO Add support for this field in the UI
      (render))))

(defmacro snippets
  "Expands to a map of HTML snippets which are extracted from the
  design templates."
  []
  {:datastore-configuration-header (snippet "datastore_configuration.html" [:#header])
   :datastore-configuration-form   (snippet "datastore_configuration.html" [:#content])
   :exercises-header               (snippet "exercises.html" [:#header])
   :exercises-content              exercises-content-template
   :exercises-list-item            exercises-list-item-template
   :new-exercise-header            (snippet "new_exercise.html" [:#header])
   :new-exercise-form              (snippet "new_exercise.html" [:#new-exercise-form])
   :new-set-header                 (snippet "new_set.html" [:#header])
   :new-set-form                   (snippet "new_set.html" [:#new-set-form])
   :set-history-div                set-history-div-template
   :set-history-list               set-history-list-template
   :set-history-list-item          set-history-list-item-template})

