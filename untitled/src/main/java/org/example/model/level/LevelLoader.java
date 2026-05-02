package org.example.model.level;

import org.example.model.enemy.EnemyType;
import org.example.model.enemy.ScheduledEnemy;
import org.example.model.point.Point;
import org.example.model.route.Route;
import org.w3c.dom.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.validation.*;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LevelLoader {

    private static final String XSD_PATH = "/level.xsd";

    public LevelData load(String xmlResourcePath) {
        try {
            validateXml(xmlResourcePath);

            InputStream xmlStream = getClass().getResourceAsStream(xmlResourcePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlStream);
            doc.getDocumentElement().normalize();

            int initialMoney      = Integer.parseInt(getTextContent(doc, "initialMoney"));
            double spawnInterval  = Double.parseDouble(getTextContent(doc, "spawnInterval"));

            List<Point> routePoints = parsePoints(doc, "route", "point");
            List<Point> towerSlots  = parsePoints(doc, "towerSlots", "slot");
            Route route = new Route(routePoints, towerSlots);

            List<ScheduledEnemy> scheduledEnemies = parseEnemies(doc);
            List<InitialTower> initialTowers = parseInitialTowers(doc);

            Level level = new Level(scheduledEnemies, spawnInterval);

            return new LevelData(level, route, initialMoney, initialTowers);

        } catch (Exception e) {
            System.err.println("Error al cargar el nivel '" + xmlResourcePath + "': " + e.getMessage());
            System.exit(1);
            return null;
        }
    }

    private void validateXml(String xmlResourcePath) throws Exception {
        InputStream xsdStream = getClass().getResourceAsStream(XSD_PATH);
        InputStream xmlStream = getClass().getResourceAsStream(xmlResourcePath);
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new StreamSource(xsdStream));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(xmlStream));
    }

    private List<Point> parsePoints(Document doc, String parentTag, String childTag) {
        List<Point> points = new ArrayList<>();
        NodeList parents = doc.getElementsByTagName(parentTag);
        if (parents.getLength() == 0) return points;
        NodeList children = ((Element) parents.item(0)).getElementsByTagName(childTag);
        for (int i = 0; i < children.getLength(); i++) {
            Element el = (Element) children.item(i);
            int x = Integer.parseInt(el.getAttribute("x"));
            int y = Integer.parseInt(el.getAttribute("y"));
            points.add(new Point(x, y));
        }
        return points;
    }

    private List<ScheduledEnemy> parseEnemies(Document doc) {
        List<ScheduledEnemy> list = new ArrayList<>();
        NodeList nodes = doc.getElementsByTagName("spawn");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element el = (Element) nodes.item(i);
            EnemyType type = EnemyType.valueOf(el.getAttribute("type"));
            int delay      = Integer.parseInt(el.getAttribute("delay"));
            list.add(new ScheduledEnemy(type, delay));
        }
        return list;
    }

    private List<InitialTower> parseInitialTowers(Document doc) {
        List<InitialTower> list = new ArrayList<>();
        NodeList nodes = doc.getElementsByTagName("tower");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element el = (Element) nodes.item(i);
            String type = el.getAttribute("type");
            int x = Integer.parseInt(el.getAttribute("slotX"));
            int y = Integer.parseInt(el.getAttribute("slotY"));
            list.add(new InitialTower(type, new Point(x, y)));
        }
        return list;
    }

    private String getTextContent(Document doc, String tag) {
        return doc.getElementsByTagName(tag).item(0).getTextContent().trim();
    }
}