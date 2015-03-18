package neuroevolution.ga.core_java;

class Report implements Runnable {
    private Thread t;
    private GA[] ga;
    private int n = 4;
    private String s;
    private boolean stop = false;

    Report(GA[] gaIN) {
        this.ga = gaIN;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (!this.stop) {
            this.show();
            Utils.sleep(Utils.rate2ms(ga[0].Report_rate));
        }
        this.show();
    }

    public void show() {
        Utils.gotorc(1, 1);
        System.out.println("Generation " + this.ga[0].IGENS + Utils.Space(n));
        // for (int x = 0; x < 1 * ga[0].POPSIZE.intValue(); x++) {
        for (int i = 0; i < Math.min(32, ga[0].POPSIZE.intValue()); i++) {
            if (i < 10)
                System.out.print(" ");
            System.out.print(i);
            // System.out.println(DataTypes.Space(n) + ga[0].population[x].getcode()
            // +
            // DataTypes.Space(n) +
            // ga[0].population[x].getvalue()+DataTypes.Space(n)+ga[0].population[x].fitness.doubleValue()+DataTypes.Space(n));
            s = "";
            if (ga[0].Report_code) {
                for (int j = 0; j < ga[0].NVAR.intValue(); j++) {
                    s += Utils.Space(n);
                    s += ga[0].population[i].getcode(j);
                }
            }
            if (ga[0].Report_value) {
                for (int j = 0; j < ga[0].NVAR.intValue(); j++) {
                    s += Utils.Space(n);
                    s += ga[0].population[i].getvalue(j).doubleValue();
                }
            }
            if (ga[0].Report_survivor[0] != ' ') {
                s += Utils.Space(n);
                s += ga[0].population[i].Survivor ? "T" : "F";
            }
            if (ga[0].Report_fitness) {
                s += Utils.Space(n);
                s += ga[0].population[i].fitness.doubleValue();
            }
            s += Utils.Space(n);
            System.out.println(s);
        }
        /*
         * for (Gen x : ga[0].population) { System.out.println(x.fitness); }
		 */
        // sleep(150);
    }

    public void check() {
        if (this.t == null)
            this.t = new Thread(this);
    }

    public void start() {
        this.check();
        this.t.start();
    }

    public void stop() {
        this.stop = true;
    }

    public boolean isAlive() {
        this.check();
        return this.t.isAlive();
    }
}
