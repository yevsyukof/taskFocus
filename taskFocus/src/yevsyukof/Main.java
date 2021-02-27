package yevsyukof;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Программа сливает отсортированные файлы.
 *
 * Предполагаеться, что данные в сливаемых файлах отсортированны соответственно режиму сортировки,
 * подающемуся на вход программе
 *
 * Если же в каком-то файле порядок данных будет нарушать предпологаемую "отсортировнность", то
 *
 * если "текущие" данные из этого файла - являються "наименьшими" "текущими данными" из всех файлов и
 * они не нарушают отсортированность в выходном файле, то они запишуться в него.
 *
 * Если же их запись нарушит отсортированность выходного файла, то эти данные будут утеряны.
 **/

public class Main {

    public static void main(String[] args) {
        ParametersParser mergeParameters;
        try {
            mergeParameters = new ParametersParser(args);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }

        Comparator<String> dataComparator = (o1, o2) -> {
            if (o2 == null) {
                return 1;
            }
            if (mergeParameters.isStringSort()) {
                if (mergeParameters.isAscendingSort()) {
                    return o1.compareTo(o2);
                } else {
                    return o2.compareTo(o1);
                }
            } else {
                if (mergeParameters.isAscendingSort()) {
                    return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
                } else {
                    return Integer.valueOf(o2).compareTo(Integer.valueOf(o1));
                }
            }
        };

        PriorityQueue<InputStackReader> priorityQueue =
                new PriorityQueue<>((o1, o2) -> dataComparator.compare(o1.peek(), o2.peek()));
        ArrayList<InputStackReader> openReaders =
                new ArrayList<>(args.length - mergeParameters.getFirstFileIdx());

        for (int i = 0; i + mergeParameters.getFirstFileIdx() < args.length; ++i) {
            InputStackReader reader = null;
            try {
                reader = new InputStackReader(args[i + mergeParameters.getFirstFileIdx()]);
                reader.pop();
                openReaders.add(reader);
                while (reader.peek() != null) {
                    try {
                        if (priorityQueue.isEmpty()) {
                            dataChecker(reader.peek(), mergeParameters);
                        }
                        priorityQueue.add(reader);
                        break;
                    } catch (NumberFormatException e) {
                        System.err.println("Wrong input data format at file: " +
                                reader.getInputFileName() + ". " + e.getMessage());
                        reader.pop();
                    }
                }
            } catch (FileNotFoundException e) {
                System.err.println(e.getLocalizedMessage());
            } catch (IOException e) {
                System.err.println(e.getLocalizedMessage());
                try {
                    openReaders.remove(openReaders.size() - 1); // возможно ошибка
                    reader.close();
                } catch (IOException readerCloseExcept) {
                    System.err.println(readerCloseExcept.getLocalizedMessage());
                }
            }
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(mergeParameters.getOutFile()))) {
            String lastWrittenData = null;
            while (priorityQueue.size() > 0) {
                InputStackReader curReader = priorityQueue.poll();
                String curData = curReader.pop(); /// xcpt mb
                if (dataComparator.compare(curData, lastWrittenData) >= 0) {
                    lastWrittenData = curData;
                    bufferedWriter.write(curData);
                    bufferedWriter.newLine();
                }
                while (curReader.peek() != null) {
                    try {
                        //TODO компаратор не вызываеться => эксепшн не выкидываеться (решено)
                        if (priorityQueue.isEmpty()) {
                            dataChecker(curReader.peek(), mergeParameters);
                        }
                        priorityQueue.add(curReader);
                        break;
                    } catch (NumberFormatException e) {
                        System.err.println("Wrong input data format at file: " +
                                curReader.getInputFileName() + ". " + e.getMessage());
                        curReader.pop();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }

        for (InputStackReader reader : openReaders) {
            try {
                reader.close();
            } catch (IOException e) {
                System.err.println(e.getLocalizedMessage());
            }
        }
    }

    static void dataChecker (String data, ParametersParser mergeParameters) {
        if (!mergeParameters.isStringSort()) {
            Integer.parseInt(data);
        }
    }
}
