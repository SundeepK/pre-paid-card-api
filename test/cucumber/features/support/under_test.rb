require 'singleton'

class UnderTest
  include Singleton

  def initialize()
    @process = ProcessHelper::ProcessHelper.new(:print_lines => true)
  end

  def start
    @process.start(["#{BASE_DIR}/run.sh cucumber"], /(Tomcat started)/)
  end

  def kill
    @process.kill
    @process.wait_for_exit
  end

end
