package android.support.v4.widget;

import android.content.Context;
import android.view.View;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

class SearchViewCompatHoneycomb {

    /* renamed from: android.support.v4.widget.SearchViewCompatHoneycomb.1 */
    static class C00211 implements OnQueryTextListener {
        final /* synthetic */ OnQueryTextListenerCompatBridge val$listener;

        C00211(OnQueryTextListenerCompatBridge onQueryTextListenerCompatBridge) {
            this.val$listener = onQueryTextListenerCompatBridge;
        }

        public boolean onQueryTextSubmit(String query) {
            return this.val$listener.onQueryTextSubmit(query);
        }

        public boolean onQueryTextChange(String newText) {
            return this.val$listener.onQueryTextChange(newText);
        }
    }

    interface OnQueryTextListenerCompatBridge {
        boolean onQueryTextChange(String str);

        boolean onQueryTextSubmit(String str);
    }

    SearchViewCompatHoneycomb() {
    }

    public static View newSearchView(Context context) {
        return new SearchView(context);
    }

    public static Object newOnQueryTextListener(OnQueryTextListenerCompatBridge listener) {
        return new C00211(listener);
    }

    public static void setOnQueryTextListener(Object searchView, Object listener) {
        ((SearchView) searchView).setOnQueryTextListener((OnQueryTextListener) listener);
    }
}
