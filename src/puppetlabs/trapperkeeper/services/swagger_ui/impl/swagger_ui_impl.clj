(ns puppetlabs.trapperkeeper.services.swagger-ui.impl.swagger-ui-impl
  (:require [schema.core :as s]
            [ring.swagger.swagger2 :as ring-swagger2]
            [ring.swagger.common :as ring-swagger-common]
            [ring.swagger.validator :as v]))

(s/defn swagger-json
  [swagger :- (s/maybe ring-swagger2/Swagger)]
  (let [[paths definitions] (-> swagger
                                ring-swagger2/ensure-body-and-response-schema-names
                                (ring-swagger2/extract-paths-and-definitions {}))]
    (let [json (ring-swagger-common/deep-merge
                ring-swagger2/swagger-defaults
                (-> swagger
                    (assoc :paths paths)
                    (assoc :definitions definitions)))]
      (if-let [validation-error (v/validate json)]
        (throw (IllegalArgumentException.
                (str "Invalid swagger json output: " validation-error))))
      json)))