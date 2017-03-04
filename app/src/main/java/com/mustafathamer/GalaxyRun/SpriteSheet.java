package com.mustafathamer.GalaxyRun;

import android.graphics.Rect;
import android.util.Log;

import com.mustafathamer.framework.FileIO;
import com.mustafathamer.framework.Graphics;
import com.mustafathamer.framework.Image;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static android.R.attr.x;


/**
 * Created by Mus on 3/2/2017.
 * Class for reading an XML-based sprite sheet.
 * Contains an Image and a list of subrects by name.
 * You can add 'matching names' so that it only stores the names you want
 */

public class SpriteSheet
{
    private Image image;
    private FileIO fileIO;
    private HashMap<String, Rect> hmap;
    private ArrayList<String> matchingNames;

    public SpriteSheet(FileIO fileIO)
    {
        this.fileIO = fileIO;
        hmap = new HashMap<>();
        matchingNames = new ArrayList<String>();
    }

    public void addMatchingName(String s)
    {
        matchingNames.add(s);
    }

    public void clearMatchingNames()
    {
        matchingNames.clear();
    }

    public boolean read(Graphics g, String xmlName, String imgName)
    {
        try
        {
            image = g.newImage(imgName, Graphics.ImageFormat.RGB565);

            InputStream is = fileIO.readAsset(xmlName);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(is);
            NodeList subTextureNodeList = document.getElementsByTagName("SubTexture");

            /*
            Now we have all the subtexture-data of spritesheet stored in subTextureList
            as an XML node. To read name,x,y, width and height of all images:
            */
            hmap.clear();
            for (int i = 0; i < subTextureNodeList.getLength(); i++)
            {
                Element subTextureElement = (Element) subTextureNodeList.item(i);
                String name = subTextureElement.getAttribute("name");
                boolean matching = true;
                if (matchingNames.size() > 0)
                {   // check if the sprite xml entry matches the ones we want
                    matching = false;
                    for (int j = 0; j < matchingNames.size(); j++)
                    {
                        if (name.contains(matchingNames.get(j)))
                        {
                            matching = true;    // found a match
                            break;
                        }
                    }
                }

                if (matching)
                {
                    int x = Integer.parseInt(subTextureElement.getAttribute("x"));
                    int y = Integer.parseInt(subTextureElement.getAttribute("y"));
                    int width = Integer.parseInt(subTextureElement.getAttribute("width"));
                    int height = Integer.parseInt(subTextureElement.getAttribute("height"));

                    Rect r = new Rect(x, y, x + width, y + height);
                    hmap.put(name, r);

                    Log.i("MOOSE", i + ". " + name + ", " + r + "\n");
                }
            }

        } catch (SAXException e)
        {
            e.printStackTrace();
            return false;
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        } catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        return true;
    }

    public Image getImage() { return image; }

    public Rect getRect(String s) { return hmap.get(s); }     // returns NULL if not found

}
