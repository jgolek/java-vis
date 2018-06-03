package server;

import static org.junit.Assert.*;

import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.Yaml;

public class TestYaml {

	@Test
	public void testName() throws Exception {
		
		DumperOptions ops = new DumperOptions();
		Yaml yaml = new Yaml(ops);

		String data = "te: 'st t'\"\ntest: test";
		System.out.println(data);
		
		String dump = yaml.dump(data.replaceAll("\n", ""));
		
		System.out.println(dump);
		
	}
	
}
