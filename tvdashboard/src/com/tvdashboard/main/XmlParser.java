package com.tvdashboard.main;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

public class XmlParser {

	public static ArrayList<String> parseXml(Context context, String category) {
		ArrayList<String> data = new ArrayList<String>();

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			AssetManager assetManager = context.getAssets();
			InputStream inputStream = assetManager.open("Extensions.xml");
			Document doc = db.parse(inputStream);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName(category);
			Node n = nList.item(0);
			NodeList childs = n.getChildNodes();

			for (int temp = 0; temp < childs.getLength(); temp++) {

				Node nNode = childs.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					data.add(nNode.getFirstChild().getNodeValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

}
