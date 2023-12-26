package task;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Main {
    private static List<AsAddr> arrayObj = new ArrayList<>();
    private static List<AsAdm> arrayHier = new ArrayList<>();
    private static final String FORMATDATE = "yyyy-MM-dd";
    private static final String FORMATLINE = "%s: %s %s";
    private static final String FINDTYPE = "проезд";
    private static final String ERRORTEXT = "Не прочитаны данные файла";

    static {
        fillArray("AS_ADDR_OBJ.XML", "OBJECT", true);
        fillArray("AS_ADM_HIERARCHY.XML", "ITEM", false);
    }

    public static void main(String[] args) {
        if (arrayObj.isEmpty() || arrayHier.isEmpty()) {
            System.out.println(ERRORTEXT);
            return;
        }

        if (args.length > 1) {
            ArrayList<String> filterId = new ArrayList<>(Arrays.asList(args));

            try {
                Date filterData = (new SimpleDateFormat(FORMATDATE)).parse(filterId.get(0));
                filterId.remove(0);
                task1(filterData, filterId);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        task2();
    }

    public static void task1(Date filterData, List<String> filterId) {
        arrayObj.stream()
                .filter(asAddr -> filterId.contains(asAddr.getObjectId()) &&
                        (filterData.after(asAddr.getStartData()) &&
                        filterData.before(asAddr.getEndData())))
                .forEach(asAddr -> {
            System.out.println(String.format(FORMATLINE, asAddr.getObjectId(), asAddr.getTypeName(), asAddr.getName()));
        });
    }

    public static void task2() {
        arrayObj.stream()
                .filter(asAddr -> asAddr.getTypeName().equals(FINDTYPE))
                .forEach(asAddr -> {
            String level = asAddr.getLevel();
            String id = asAddr.getObjectId();
            String finalName = asAddr.getTypeName() + " " + asAddr.getName();

            while(!level.equals("1")) {
                try {
                    String finalId = id;
                    String finalID1 = arrayHier.stream()
                            .filter(asAdm -> asAdm.getObjectId().equals(finalId))
                            .toList().get(0).getParentObjId();
                    AsAddr find = arrayObj.stream()
                            .filter(asAddr1 -> asAddr1.getObjectId().equals(finalID1))
                            .toList().get(0);
                    finalName = find.getTypeName() + " " + find.getName() + ", " + finalName;
                    level = find.getLevel();
                    id = find.getObjectId();
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    break;
                }
            }

            System.out.println(finalName);
        });
    }

    public static void fillArray(String fileName, String tagName, boolean fillArrayObj) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(fileName));
            NodeList employeeElements = document.getDocumentElement().getElementsByTagName(tagName);

            for(int i = 0; i < employeeElements.getLength(); ++i) {
                Node node = employeeElements.item(i);
                if (fillArrayObj) {
                    arrayObj.add(new AsAddr(node.getAttributes()));
                } else {
                    arrayHier.add(new AsAdm(node.getAttributes()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
