package yevsyukof;

public class ParametersParser {
    private boolean isAscendingSort = true;
    private boolean stringSort = true;
    private String outFile = null;
    private int firstFileIdx = 0;

    ParametersParser(String[] args) throws Exception {
        if (args.length < 3) {
            throw new Exception("Wrong program input!");
        }
        for (int param = 0, paramOrder = 1; param < args.length; ++param) {
            if (paramOrder == 4){
                firstFileIdx = param;
                break;
            }
            switch (paramOrder) {
                case 1 -> {
                    if (args[param].equals("-d")) {
                        this.isAscendingSort = false;
                        ++paramOrder;
                    } else if (args[param].equals("-s")) {
                        paramOrder += 2;
                    } else if (args[param].equals("-i")) {
                        this.stringSort = false;
                        paramOrder += 2;
                    } else if (!args[param].equals("-a")) {
                        throw new Exception("Wrong input param!");
                    }
                }
                case 2 -> {
                    if (args[param].equals("-s")) {
                        ++paramOrder;
                    } else if (args[param].equals("-i")) {
                        stringSort = false;
                        ++paramOrder;
                    } else {
                        throw new Exception("Wrong input param! Data type do not specified!");
                    }
                }
                case 3 -> {
                    outFile = args[param];
                    ++paramOrder;
                }
            }
        }
    }

    public boolean isAscendingSort() {
        return isAscendingSort;
    }

    public boolean isStringSort() {
        return stringSort;
    }

    public String getOutFile() {
        return outFile;
    }

    public int getFirstFileIdx() {
        return firstFileIdx;
    }
}
