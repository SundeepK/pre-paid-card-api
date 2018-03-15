require 'pry'
require 'json_spec/cucumber'
require 'process-helper'
require_relative 'under_test'

JsonSpec.configure do
  exclude_keys 'epochMillis'
end

AfterConfiguration do |config|
  UnderTest.instance.start
end

After do |scenario|
  RestClient.get "http://localhost:8080/reset"
end

at_exit do
  UnderTest.instance.kill
end

