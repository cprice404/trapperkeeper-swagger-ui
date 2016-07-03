(ns puppetlabs.trapperkeeper.services.swagger-ui.impl.swagger-ui-impl
  (:require [schema.core :as schema]
            [ring.swagger.swagger2 :as ring-swagger2]
            [ring.swagger.swagger2-full-schema :as ring-swagger2-full-schema]
            [ring.swagger.common :as ring-swagger-common]
            [ring.swagger.validator :as v]))

;; this stuff was originally copied from ring.swagger.swagger2/swagger-json;
;; modifying to allow :info and :paths to be converted independently, so that
;; we can support both schema and spec.

(def PathsAndDefinitions
  ; TODO: this could be tightened up; it represents the final JSON structure
  ; for paths and definitions, but the ring-swagger library doesn't have a schema
  ; for these.
  {:paths schema/Any
   :definitions schema/Any})

(schema/defn ^:always-validate convert-schema-paths :- PathsAndDefinitions
  [paths :- ring-swagger2-full-schema/Paths]
  (let [[paths definitions] (-> {:paths paths}
                                ring-swagger2/ensure-body-and-response-schema-names
                                (ring-swagger2/extract-paths-and-definitions ring-swagger2/option-defaults))]
    {:paths paths
     :definitions definitions}))

(schema/defn ^:always-validate swagger-json
  [info :- ring-swagger2-full-schema/Info
   {:keys [paths definitions]} :- PathsAndDefinitions]
  (let [json (ring-swagger-common/deep-merge
              ring-swagger2/swagger-defaults
              {:info info
               :paths paths
               :definitions definitions})]
    (if-let [validation-error (v/validate json)]
      (throw (IllegalArgumentException.
              (str "Invalid swagger json output: " validation-error))))
    json))