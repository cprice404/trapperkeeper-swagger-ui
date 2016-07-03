(ns puppetlabs.trapperkeeper.services.swagger-ui.swagger-ui-service
  (:require [puppetlabs.trapperkeeper.core :as tk]
            [puppetlabs.trapperkeeper.services :as tks]
            [puppetlabs.trapperkeeper.protocols.swagger-ui :refer [SwaggerUIService]]
            [puppetlabs.trapperkeeper.services.swagger-ui.swagger-ui-core :as swagger-ui-core]
            [ring.swagger.ui :as ring-swagger-ui]
            [ring.middleware.json :as ring-json]))

(tk/defservice swagger-ui-service
  SwaggerUIService
  [[:ConfigService get-in-config]
   [:WebroutingService add-ring-handler get-route]]
  (init [this context]
        (let [config (swagger-ui-core/initialize-config (get-in-config [:swagger-ui]))
              registered-schema-paths (atom {})
              registered-tags (atom {})
              swagger-ui-route (get-route this :swagger)]
          (add-ring-handler this (ring-swagger-ui/swagger-ui swagger-ui-route)
                            {:route-id :swagger})
          (add-ring-handler this (ring-json/wrap-json-response
                                  (swagger-ui-core/swagger-json-handler
                                   (:info config)
                                   registered-schema-paths))
                            {:route-id :swagger-json})
          (-> context
              (assoc :registered-schema-paths registered-schema-paths
                     :registered-tags registered-tags))))
  (register-tags [this tags]
                 (let [context (tks/service-context this)]
                   (swagger-ui-core/register-tags
                    (:registered-tags context)
                    tags)))
  (register-schema-paths [this paths]
                         (let [context (tks/service-context this)]
                           (swagger-ui-core/register-schema-paths
                            (:registered-schema-paths context)
                            paths))))
