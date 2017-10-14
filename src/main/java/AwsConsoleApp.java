import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;

public class AwsConsoleApp {

	static AmazonEC2Client amazonEC2Client;

	public static void main(String args[]) throws IOException {
		
		//load aws credentials from properties file
		AWSCredentials credentials = new PropertiesCredentials(
				AwsConsoleApp.class
						.getResourceAsStream("AwsCredentials.properties"));
		System.out.println("after loading aws credentials");
		
		//create ec2 client using the credentials
		amazonEC2Client = new AmazonEC2Client(credentials);
		System.out.println("created amazonEC2Client");

		//set server location
		amazonEC2Client.setEndpoint("ec2.us-west-2.amazonaws.com");

		//create filter to fetch instance list by tag key
		Filter filter = new Filter();
		String tagFilterName = "tag-key";//tag-key is predefined name of filter to use when using tag keys
		filter.setName(tagFilterName);
		
		String[] tagKey = { "Name" };//specify the name of tags key here
		filter.setValues(Arrays.asList(tagKey));
		
		//create a instance request object and set filter to it
		DescribeInstancesRequest request = new DescribeInstancesRequest();
		request.setFilters(Arrays.asList(filter));

		//perform aws query to get the instance result
		DescribeInstancesResult response = amazonEC2Client
				.describeInstances(request);
		System.out.println("after requesting aws for instances result");
		
		//now iterate over the result
		List<Reservation> reservations = response.getReservations();
		for (Reservation reservation : reservations) {
			for (Instance instance : reservation.getInstances()) {
				System.out.println("instance keyname=" + instance.getKeyName()
						+ ";id=" + instance.getInstanceId() + ";tags="
						+ instance.getTags());
			}
		}
	}
}
