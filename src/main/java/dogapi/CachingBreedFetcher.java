package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private int callsMade = 0;
    BreedFetcher breedFetcher;
    HashMap<String, List<String>> breedToSubBreed = new HashMap<>();

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.breedFetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // return statement included so that the starter code can compile and run.


        if (!breedToSubBreed.containsKey(breed)) {
            try {
                this.callsMade++;
                List<String> subBreeds = breedFetcher.getSubBreeds(breed);
                breedToSubBreed.put(breed, subBreeds);
            } catch (BreedNotFoundException e) {
                throw new BreedNotFoundException(e.getMessage());
            }
        }

        return breedToSubBreed.get(breed);
    }

    public int getCallsMade() {
        return callsMade;
    }
}