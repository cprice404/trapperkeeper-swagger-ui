(def ks-version "1.3.1")
(def tk-version "1.4.1")
(def tk-jetty-version "1.5.9")

(defproject puppetlabs/trapperkeeper-swagger-ui "0.1.0-SNAPSHOT"
  :description "Trapperkeeper service for hosting swagger-ui in a TK app"
  :url "http://github.com/cprice404/trapperkeeper-swagger-ui"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0.html"}

  ;; Abort when version ranges or version conflicts are detected in
  ;; dependencies. Also supports :warn to simply emit warnings.
  ;; requires lein 2.2.0+.
  :pedantic? :abort

  :dependencies [[org.clojure/clojure "1.8.0"]

                 ;; begin version conflict resolution dependencies
                 [clj-time "0.12.0"]
                 [ring/ring-codec "1.0.1"]
                 [commons-codec "1.9"]
                 [ring/ring-json "0.4.0"]
                 [cheshire "5.6.1"]
                 [ring/ring-core "1.5.0"]
                 [org.clojure/tools.reader "1.0.0-beta2"]
                 ;; end version conflict resolution dependencies

                 [prismatic/schema "1.1.2"]
                 [metosin/ring-swagger "0.22.9"]
                 [metosin/ring-swagger-ui "2.1.4-0"]

                 [puppetlabs/trapperkeeper ~tk-version]]

  :profiles {:dev {:dependencies [[puppetlabs/kitchensink ~ks-version :classifier "test" :scope "test"]
                                  [puppetlabs/trapperkeeper ~tk-version :classifier "test" :scope "test"]
                                  [puppetlabs/trapperkeeper-webserver-jetty9 ~tk-jetty-version]
                                  [puppetlabs/http-client "0.5.0"]]}}

  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env/clojars_jenkins_username
                                     :password :env/clojars_jenkins_password
                                     :sign-releases false}]
                        ["snapshots" "http://nexus.delivery.puppetlabs.net/content/repositories/snapshots/"]]
  )
