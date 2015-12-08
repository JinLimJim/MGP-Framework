package com.limjin.mobileg2015;

import android.content.Context;

import java.util.LinkedList;

/**
 * Created by tanyiecher on 6/12/2015.
 */

/*************************************************************************************
 Manages rubbish.
 *************************************************************************************/
public class RubbishManager
{
    private LinkedList<Rubbish> rubbishPile = new LinkedList<Rubbish>();

    public RubbishManager(int TotalRubbish, Context context)
    {
        for(int i = 0; i < TotalRubbish; ++i)
        {
           rubbishPile.add(new Rubbish(0.f, 0.f, 50, 50, Rubbish.TYPE.RECYCLABLE, 50.f, context));
        }
    }

    public void Init()
    {
        for(int i = 0; i < rubbishPile.size(); ++i)
        {
            //set/reset all elements
            rubbishPile.get(i).Set(0.f, 0.f, Rubbish.TYPE.RECYCLABLE, 50.f);
        }
    }

    public Rubbish GetRubbish(Context context, Rubbish.TYPE type)
    {
        //iterate thru to get a not active one---------------------------//
        int returnIndex = -1;
        for(int i = 0; i < rubbishPile.size(); ++i)
        {
            if(!rubbishPile.get(i).GetActive())
            {
                rubbishPile.get(i).SetType(Rubbish.TYPE.RECYCLABLE);
                returnIndex = i;
                break;
            }
        }

        //not enough :((((((((((((((((((--------------------------------//
        if(returnIndex == -1)
        {
            returnIndex = rubbishPile.size();

            for (int i = 0; i < 10; ++i) {
                rubbishPile.add(new Rubbish(0.f, 0.f, 50, 50, Rubbish.TYPE.RECYCLABLE, 50.f, context));
            }
        }

        //return this-----------------------------------------------//
        rubbishPile.get(returnIndex).SetType(type);
        rubbishPile.get(returnIndex).Activate();
        return rubbishPile.get(returnIndex);
    }
}
