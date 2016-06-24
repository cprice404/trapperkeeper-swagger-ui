(ns puppetlabs.trapperkeeper.services.swagger-ui.swagger-ui-service-test
  (:require [clojure.test :refer :all]
            [puppetlabs.trapperkeeper.testutils.bootstrap :refer [with-app-with-config]]
            [puppetlabs.trapperkeeper.services.swagger-ui.swagger-ui-service :refer [swagger-ui-service]]
            [puppetlabs.trapperkeeper.services.webrouting.webrouting-service :refer [webrouting-service]]
            [puppetlabs.trapperkeeper.services.webserver.jetty9-service :refer [jetty9-service]]
            [puppetlabs.http.client.sync :as http-client]
            [puppetlabs.trapperkeeper.core :as tk]
            [schema.core :as schema]
            [schema.test :as schema-test]))

(use-fixtures :once schema-test/validate-schemas)

(schema/defschema Foo
  {:foo schema/Str
   :bar schema/Str})

(tk/defservice foo-web-service
  [[:SwaggerUIService register-tags register-paths]]
  (init [this context]
        (register-tags [{:name "foo1"
                         :description "Foo1 Desc"}
                        {:name "foo2"
                         :description "Foo2 Desc"}])
        (register-paths {"/foo1/ping" {:get {:tags ["foo1"]}}
                         "/foo2/:id" {:post {:summary "Foo Api"
                                             :description "Foo Api description"
                                             :tags ["foo2"]
                                             :parameters {:path {:id schema/Str}
                                                          :body Foo}
                                             :responses {200 {:schema Foo
                                                              :description "Success!"}
                                                         404 {:description "Fail!"}}}}})
        context))

(deftest swagger-ui-test
  (testing "swagger ui is served with registered paths"
    (with-app-with-config app
      [swagger-ui-service
       webrouting-service
       jetty9-service
       foo-web-service]
      {:webserver {:port 8000}
       :web-router-service
       {:puppetlabs.trapperkeeper.services.swagger-ui.swagger-ui-service/swagger-ui-service
        {:swagger "/docs"
         :swagger-json "/swagger.json"}}
       :swagger-ui {:info {:title "Test App"}}}
      (let [response (http-client/get "http://localhost:8000/docs")]
        (is (= 200 (:status response)))))))
