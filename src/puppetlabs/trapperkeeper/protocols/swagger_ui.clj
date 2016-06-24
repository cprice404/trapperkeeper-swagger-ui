(ns puppetlabs.trapperkeeper.protocols.swagger-ui)

(defprotocol SwaggerUIService
  (register-tags [this tags])
  (register-paths [this paths]))
