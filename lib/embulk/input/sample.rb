Embulk::JavaPlugin.register_input(
  "sample", "org.embulk.input.sample.SampleInputPlugin",
  File.expand_path('../../../../classpath', __FILE__))
