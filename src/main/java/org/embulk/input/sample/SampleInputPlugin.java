package org.embulk.input.sample;

import org.embulk.config.ConfigDiff;
import org.embulk.config.ConfigSource;
import org.embulk.config.Task;
import org.embulk.config.TaskReport;
import org.embulk.config.TaskSource;
import org.embulk.spi.Exec;
import org.embulk.spi.InputPlugin;
import org.embulk.spi.PageBuilder;
import org.embulk.spi.PageOutput;
import org.embulk.spi.Schema;
import org.embulk.spi.time.Timestamp;
import org.msgpack.value.ValueFactory;

import java.util.List;

import static org.embulk.spi.type.Types.BOOLEAN;
import static org.embulk.spi.type.Types.DOUBLE;
import static org.embulk.spi.type.Types.JSON;
import static org.embulk.spi.type.Types.LONG;
import static org.embulk.spi.type.Types.STRING;
import static org.embulk.spi.type.Types.TIMESTAMP;

public class SampleInputPlugin
        implements InputPlugin
{
    public interface PluginTask
            extends Task
    {
//        // configuration option 1 (required integer)
//        @Config("option1")
//        public int getOption1();
//
//        // configuration option 2 (optional string, null is not allowed)
//        @Config("option2")
//        @ConfigDefault("\"myvalue\"")
//        public String getOption2();
//
//        // configuration option 3 (optional string, null is allowed)
//        @Config("option3")
//        @ConfigDefault("null")
//        public Optional<String> getOption3();
//
//        // if you get schema from config
//        @Config("columns")
//        public SchemaConfig getColumns();
    }

    @Override
    public ConfigDiff transaction(ConfigSource config,
            InputPlugin.Control control)
    {
        PluginTask task = config.loadConfig(PluginTask.class);

        Schema.Builder builder = new Schema.Builder();
        builder.add("string", STRING)
                .add("boolean", BOOLEAN)
                .add("long", LONG)
                .add("double", DOUBLE)
                .add("timestamp", TIMESTAMP)
                .add("json", JSON);

        Schema schema = builder.build();
        int taskCount = 1;  // number of run() method calls

        return resume(task.dump(), schema, taskCount, control);
    }

    @Override
    public ConfigDiff resume(TaskSource taskSource,
            Schema schema, int taskCount,
            InputPlugin.Control control)
    {
        control.run(taskSource, schema, taskCount);
        System.out.println("Resume called");
        return Exec.newConfigDiff();
    }

    @Override
    public void cleanup(TaskSource taskSource,
            Schema schema, int taskCount,
            List<TaskReport> successTaskReports)
    {
        System.out.println("Cleanup called");
    }

    @Override
    public TaskReport run(TaskSource taskSource,
            Schema schema, int taskIndex,
            PageOutput output)
    {
        PluginTask task = taskSource.loadTask(PluginTask.class);

        try (final PageBuilder builder = new PageBuilder(Exec.getBufferAllocator(), schema, output)) {
            for (int i = 0; i < 10; i++) {
                builder.setString(0, "string");
                builder.setBoolean(1, true);
                builder.setLong(2, 10);
                builder.setDouble(3, 3.14);
                builder.setTimestamp(4, Timestamp.ofEpochSecond(1451606400));
                builder.setJson(5, ValueFactory.newString("json"));
                builder.addRecord();
            }
            builder.finish();
        }
        return Exec.newTaskReport();
    }

    @Override
    public ConfigDiff guess(ConfigSource config)
    {
        return Exec.newConfigDiff();
    }
}
