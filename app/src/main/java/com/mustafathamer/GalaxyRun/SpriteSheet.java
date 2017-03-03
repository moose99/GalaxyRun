package com.mustafathamer.GalaxyRun;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static android.R.attr.data;
import static android.R.attr.name;
import static android.R.attr.x;
import static android.R.attr.y;

/**
 * Created by Mus on 3/2/2017.
 * Class for reading an XML-based sprite sheet
 */

public class SpriteSheet
{

    public SpriteSheet()
    {

    }

    public boolean Read(InputStream is)
    {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try
        {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(is);
            NodeList subTextureNodeList = document.getElementsByTagName("SubTexture");

            /*

            Now we have all the subtexture-data of spritesheet stored in subTextureList
            as an XML node. To read name,x,y, width and height of all images:

            */
            for(int i = 0 ; i < subTextureNodeList.getLength(); i++)
            {
                Element subTextureElement = (Element)subTextureNodeList.item(i);
                String name = subTextureElement.getAttribute("name");
                String x= subTextureElement.getAttribute("x");
                String y= subTextureElement.getAttribute("y");
                String width = subTextureElement.getAttribute("width");
                String height= subTextureElement.getAttribute("height");

                // DO SOMETHING WITH THIS DATA HERE
                // GET THE PICTURE FROM SHEET WITH THESE.

                Log.i("MOOSE", name + ", " + x + ", " + ", " + y + ", " + width + ", " + height + "\n");
            }

        }
        catch (SAXException e)
        {
            e.printStackTrace();
            return false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        } catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        return true;
    }

}
