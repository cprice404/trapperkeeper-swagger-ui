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
                 [clj-time "0.9.0"]
                 ;; end version conflict resolution dependencies

                 [prismatic/schema "1.1.1"]
                 [metosin/ring-swagger "0.22.9"]

                 [puppetlabs/trapperkeeper ~tk-version]]

  :profiles {:dev {:dependencies [[puppetlabs/trapperkeeper-webserver-jetty9 ~tk-jetty-version]
                                  [puppetlabs/trapperkeeper ~tk-version :classifier "test" :scope "test"]]}}

  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env/clojars_jenkins_username
                                     :password :env/clojars_jenkins_password
                                     :sign-releases false}]
                        ["snapshots" "http://nexus.delivery.puppetlabs.net/content/repositories/snapshots/"]]
  )
