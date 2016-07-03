(ns puppetlabs.trapperkeeper.services.swagger-ui.swagger-ui-core
  (:require [schema.core :as schema]
            [ring.swagger.swagger2-schema :as swagger2-schema]
            [ring.swagger.swagger2-full-schema :as swagger2-full-schema]
            [puppetlabs.trapperkeeper.services.swagger-ui.impl.swagger-ui-impl :as swagger-ui-impl]))

(def SwaggerUIConfig
  {:info swagger2-schema/Info})

(def RegisteredTags
  {schema/Str swagger2-full-schema/Tag})

(schema/defn ^:always-validate initialize-config :- SwaggerUIConfig
  [config :- SwaggerUIConfig]
  config)

(schema/defn ^:always-validate register-tags :- RegisteredTags
  [existing-tags :- (schema/atom RegisteredTags)
   tags :- [swagger2-full-schema/Tag]]
  (swap! existing-tags merge (into {} (map (juxt :name identity) tags))))

(schema/defn ^:always-validate register-paths :- swagger2-full-schema/Paths
  [existing-paths :- (schema/atom swagger2-full-schema/Paths)
   paths :- swagger2-full-schema/Paths]
  (swap! existing-paths merge paths))

(defn swagger-json-handler
  [content registered-paths]
  (fn [req]
    {:body (swagger-ui-impl/swagger-json
            (assoc content :paths @registered-paths))}))