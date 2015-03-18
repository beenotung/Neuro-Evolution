package neuroevolution.ga.core_java;

import java.util.Arrays;

public class SelectX {

    private GA[] ga;
    private SelectXThreads[] selectXThreads = new SelectXThreads[GA.NThread];

    SelectX(GA[] gaIN) {
        this.ga = gaIN;
    }

    public void run() {
        Arrays.sort(ga[0].population);

        for (int i = 0; i < GA.NThread; i++) {
            selectXThreads[i] = new SelectXThreads(i);
            selectXThreads[i].start();
        }

        while (Utils.someAlive(selectXThreads))
            Utils.sleep();

        // for (int x = 0; x < ga[0].population.length; x++) {
        // ga[0].population[x].Survivor = ((double) x / ga[0].population.length)
        // <= ga[0].CUTOFF;
        // }
        ga[0].population[0].Survivor = true;
        ga[0].population[1].Survivor = true;
    }

    public class SelectXThreads extends MyRunnable {
        SelectXThreads(int IThreadIN) {
            super(IThreadIN, ga[0].POPSIZE.intValue());
        }

        @Override
        public void action(int i) {
            ga[0].population[i].Survivor = ((double) i / ga[0].population.length) <= ga[0].CUTOFF;
        }
    }
}
