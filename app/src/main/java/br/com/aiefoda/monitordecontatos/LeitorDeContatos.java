package br.com.aiefoda.monitordecontatos;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;


public class LeitorDeContatos implements LoaderManager.LoaderCallbacks {

    private int totalDeContatos = 0;
    private Context contexto;

    public LeitorDeContatos(Context contexto, LoaderManager loaderManager) {
        this.contexto = contexto;
        loaderManager.initLoader(0, null, this);
    }

    public int contaContatos() {
        return totalDeContatos;
    }

    @Override
    public Loader onCreateLoader(int arg0, Bundle arg1) {
        CursorLoader cursorLoader = new CursorLoader(contexto, ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID},null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        this.totalDeContatos = cursor.getCount();
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

    }

    @Override
    public void onLoaderReset(Loader arg0) {

    }

}
