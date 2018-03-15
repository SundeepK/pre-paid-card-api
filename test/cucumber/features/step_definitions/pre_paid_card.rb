require 'json'
require 'rest-client'

def get(url)
  begin
    @response = RestClient.get url
  rescue RestClient::ExceptionWithResponse => e
    @response = e.response
  end
end

def post(url, payload)
  begin
    @response = RestClient.post(url, payload.to_json, content_type: :json)
  rescue RestClient::ExceptionWithResponse => e
    @response = e.response
  end
end

When(/^I GET ([^"]*)$/) do |path|
  get("http://localhost:8080/#{path}")
end

Then(/^response is a "([^"]*)"$/) do |response_code|
  expect(@response.code.to_s).to eq(response_code)
end

def last_json
  @response
end

When(/^I POST ([^"]*):$/) do |path, table|
  table.hashes.each do |hash|
    post("http://localhost:8080/#{path}", hash)
  end
end