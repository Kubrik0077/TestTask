package task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class AsAddr {
    private String name;
    private String objectId;
    private String typeName;
    private String level;
    private Date startData;
    private Date endData;

    public AsAddr(NamedNodeMap nodeMap) throws ParseException {
        for(int i = 0; i < nodeMap.getLength(); ++i) {
            SimpleDateFormat dateParse = new SimpleDateFormat("yyyy-MM-dd");
            Node node = nodeMap.item(i);
            switch (node.getNodeName()) {
                case "ENDDATE" -> this.endData = dateParse.parse(node.getNodeValue());
                case "NAME" -> this.name = node.getNodeValue();
                case "OBJECTID" -> this.objectId = node.getNodeValue();
                case "STARTDATE" -> this.startData = dateParse.parse(node.getNodeValue());
                case "TYPENAME" -> this.typeName = node.getNodeValue();
                case "LEVEL" -> this.level = node.getNodeValue();
                default -> {}
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getLevel() {
        return level;
    }

    public Date getStartData() {
        return startData;
    }

    public Date getEndData() {
        return endData;
    }
}
