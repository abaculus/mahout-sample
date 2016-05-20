package se.panok.mahout;

import java.io.File;
import java.util.List;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class MovieRecommender {

	private MovieRecommender() {
	}

	public static void main(String[] args) throws Exception {

		final DataModel model = new FileDataModel(getFile("movie-data.csv"));

		/*
		 * Here we are using Pearson's Correlation it has limitations such as
		 * that it does not account for the NUMBER of items in which 2 users'
		 * preferences overlap. There are other options such as Euclidean
		 * Distance similarity.
		 */
		final boolean isPearson = true;
		UserSimilarity similarity = isPearson ? new PearsonCorrelationSimilarity(model)
				: new EuclideanDistanceSimilarity(model);

		/*
		 * First parameter - neighborhood size; capped at the number of users in
		 * the data model
		 */
		final int neighborhoodSize = 5;
		final UserNeighborhood neighborhood = new NearestNUserNeighborhood(neighborhoodSize, similarity, model);

		final Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

		/*
		 * First parameter is UserID, the second is the number of items to be recommended.
		 */
		final int userIdToGetRecommendationsFor = 1;
		final int numberOfRecommendations = 2;
		final List<RecommendedItem> recommendations = recommender.recommend(userIdToGetRecommendationsFor,
				numberOfRecommendations);

		for (final RecommendedItem recommendation : recommendations) {
			System.out.printf("User with id = %1d is recommended movie %2s\r\n", userIdToGetRecommendationsFor,
					recommendation);
			System.out.flush();
		}

	}

	private static File getFile(final String resourceItemName) {
		ClassLoader classLoader = MovieRecommender.class.getClassLoader();
		return new File(classLoader.getResource(resourceItemName).getFile());
	}
}
