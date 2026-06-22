package com.github.cdeor.dating_ai_backend.profiles;

import com.github.cdeor.dating_ai_backend.utils.Utils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfileService {

    private SearchCriteria searchCriteria;

    @Value("${diffusion.url}")
    private static String DIFFUSION_URL;

    private ChatClient chatClient;

    private HttpClient httpClient;

    private HttpRequest.Builder diffusionRequestBuilder;

    private List<Profile> allProfiles = new ArrayList<>();

    @Value("${profiles.path}")
    private static String PROFILES_PATH = "profiles.json";

    @Value("${startup.initializeProfiles}")
    private Boolean initializeProfiles;

    @Value("#{${dating.ai.character.user}}")
    private Map<String, String> userProfileProperties;

    private ProfileRepository profileRepository;

    public ProfileService(ChatClient chatClient,
                          ProfileRepository profileRepository,
                          SearchCriteria searchCriteria) {
        this.chatClient = chatClient;
        this.profileRepository = profileRepository;
        this.searchCriteria = searchCriteria;
        this.httpClient = HttpClient.newHttpClient();
        this.diffusionRequestBuilder = HttpRequest.newBuilder()
                .setHeader("Content-type", "application/json")
                .uri(URI.create(DIFFUSION_URL));
    }


    public void generateProfiles(int numberOfProfiles) {

        if (!this.initializeProfiles) {
            return;
        }

        // Identify the age range, genders and ethnicities for generating profiles
        List<Integer> ages = new ArrayList<>();
        for (int i = searchCriteria.getStartAge(); i <= searchCriteria.getEndAge(); i++) {
            ages.add(i);
        }
        List<String> ethnicities = new ArrayList<>(List.of("Indian", "Asian"));
        List<String> personalityTypes = Utils.generatePersonalityTypes();
        String gender = searchCriteria.getLookingForGender();

        while (this.allProfiles.size() < numberOfProfiles) {
            int age = Utils.getRandomElement(ages);
            String ethnicity = Utils.getRandomElement(ethnicities);
            String personalityType = Utils.getRandomElement(personalityTypes);

            String promptStr = "Create a Dating profile persona for a " + personalityType + " " + +age + " years old " + ethnicity + " " + gender + " "
                    + " including the first name, last name, Myers Briggs Personality type and a tinder bio. Save the profile using the saveProfile function";

            //      Make a call to OpenAI to generate a sample Tinder profile for these variables
            Prompt prompt = new Prompt(promptStr);


            ChatResponse response = chatClient.prompt(prompt).call().chatResponse();
        }
        // Save the values in a JSON file
    }


}
