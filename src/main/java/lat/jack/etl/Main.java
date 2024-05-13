package lat.jack.etl;

import lat.jack.etl.etl.ETLProcessor;

public class Main {
    public static void main(String[] args) {
        ETLProcessor etlProcessor = new ETLProcessor();
        etlProcessor.process();
    }
}