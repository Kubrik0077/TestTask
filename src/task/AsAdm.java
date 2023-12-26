package task;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class AsAdm {
    private String objectId;
    private String parentObjId;

    public AsAdm(NamedNodeMap nodeMap) {
        for(int i = 0; i < nodeMap.getLength(); ++i) {
            Node node = nodeMap.item(i);
            switch (node.getNodeName()) {
                case "OBJECTID" -> this.objectId = node.getNodeValue();
                case "PARENTOBJID" -> this.parentObjId = node.getNodeValue();
                default -> {}
            }
        }
    }

    public String getObjectId() {
        return objectId;
    }

    public String getParentObjId() {
        return parentObjId;
    }
}
