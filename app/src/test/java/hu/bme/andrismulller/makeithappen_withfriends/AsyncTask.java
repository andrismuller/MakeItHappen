package hu.bme.andrismulller.makeithappen_withfriends;

/**
 * Created by Muller Andras on 4/22/2018.
 */

public abstract class AsyncTask<Params, Progress, Result> {
	protected abstract Result doInBackground(Params... params);

	protected void onPostExecute(Result result) {
	}

	protected void onProgressUpdate(Progress... values) {
	}

	public AsyncTask<Params, Progress, Result> execute(Params... params) {
		Result result = doInBackground(params);
		onPostExecute(result);
		return this;
	}
}
