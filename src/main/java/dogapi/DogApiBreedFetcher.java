package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();
    private static final String API_URL = "https://dog.ceo/api/breed/";

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        String html = API_URL + breed + "/list";

        Request request = new Request.Builder().url(html).build();

        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                JSONObject responseBody = new JSONObject(response.body().string());

                JSONArray breeds = responseBody.getJSONArray("message");
                ArrayList<String> subBreeds = new ArrayList<>();

                for (int i = 0; i < breeds.length(); i++) {
                    subBreeds.add(breeds.getString(i));
                }

                return subBreeds;

            } else {
                throw new BreedNotFoundException("Couldn't find breed for " + breed);
            }
        } catch (IOException e) {
            throw new BreedNotFoundException("Couldn't make request");
        }
    }

    public static void main(String[] args) {
        DogApiBreedFetcher breedFetcher = new DogApiBreedFetcher();
        try {
            System.out.println(breedFetcher.getSubBreeds("collie"));
        } catch (BreedNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}