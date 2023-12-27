package com.example.shapeforge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager2.widget.ViewPager2;

import com.example.shapeforge.Login_Register.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private TextView workoutIcon, TodayWorkoutTV, stat1, stat2, stat3, stat4, Title;

    private AppCompatButton TodayWorkoutButton;

    private ImageButton profileButton, settingButton;

    private String Name,Username, Email;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private MyViewPagerAdapter myViewPagerAdapter;

    private FirebaseAuth auth;

    private FirebaseUser user;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private ReadAndWriteSnippets snippets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Title = findViewById(R.id.title_main);
        settingButton = findViewById(R.id.settingsButton);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();



        if( user == null){
            Intent intentNullUser = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intentNullUser);
            finish();
        }



        database = FirebaseDatabase.getInstance("https://shape-forge-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference();

        snippets = new ReadAndWriteSnippets(reference);


        /*
        if(GlobalClass.user.getName().isEmpty()){
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

         */



        /*
        snippets.getUser(user.getUid(), new ReadAndWriteSnippets.OnUserRetrieveListener() {
            @Override
            public void onUserRetrieved(User user) {
                GlobalClass.user = user;
            }

            @Override
            public void onUserNotFound() {
                // Handle the scenario where the user is not found
            }

            @Override
            public void onUserRetrieveError(String error) {
                // Handle the error scenario
            }
        });

         */

        snippets.getUserBadges(user.getUid(), new ReadAndWriteSnippets.OnUserBadgesRetrieveListener() {
            @Override
            public void onUserBadgesRetrieved(Map<String, Badge> badges) {
                GlobalClass.badges = badges;
            }

            @Override
            public void onUserBadgesNotFound() {

            }

            @Override
            public void onUserBadgesError(String error) {

            }
        });

        snippets.getPRsForUser(user.getUid(), new ReadAndWriteSnippets.OnPRsRetrieveListener() {

            @Override
            public void onPRsRetrieved(Map<String, Long> PRs) {
                GlobalClass.PRs = PRs;
            }

            @Override
            public void onPRsNotFound() {
                // No PRs found for the user
                // Handle this case
            }

            @Override
            public void onUserNotFound() {
                // User not found
                // Handle this case
            }

            @Override
            public void onPRsError(String error) {
                // Handle error case
            }
        });

        snippets.getWorkoutList(user.getUid(), new ReadAndWriteSnippets.OnWorkoutListListener() {
            @Override
            public void onWorkoutListRetrieved(List<Workout> workoutList) {
                // Handle successful retrieval of the workout list
                GlobalClass.workoutList = workoutList;
            }

            @Override
            public void onWorkoutListNotFound() {

            }

            @Override
            public void onUserNotFound() {

            }

            @Override
            public void onWorkoutListError(String error) {

            }
        });


// Retrieve user plans
        snippets.getUserPlans(user.getUid(), new ReadAndWriteSnippets.OnUserPlansRetrieveListener() {
            @Override
            public void onUserPlansRetrieved(Map<LocalDate, String> plansList) {
                GlobalClass.plansList = plansList;
            }

            @Override
            public void onUserPlansNotFound() {
                // Handle scenario where plans are not found
            }

            @Override
            public void onUserPlansError(String error) {
                // Handle error
            }
        });

        addExercises();

        addBadges();


        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 =findViewById(R.id.view_pager);
        myViewPagerAdapter = new MyViewPagerAdapter(this);
        viewPager2.setAdapter(myViewPagerAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });


        getWindow().setWindowAnimations(android.R.style.Animation_Translucent);


        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent enterSettingsIntent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(enterSettingsIntent);
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.destination_home); // Set the ID of the desired menu item

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.destination_badges:
                        Intent badgesIntent = new Intent(MainActivity.this, CalculatorActivity.class);
                        startActivity(badgesIntent);
                        return true;
                    case R.id.destination_workout:
                        Intent workoutIntent = new Intent(MainActivity.this, WorkoutActivity.class);
                        startActivity(workoutIntent);
                        return true;
                    case R.id.destination_plan:
                        Intent planIntent = new Intent(MainActivity.this, PlansActivity.class);
                        startActivity(planIntent);
                        return true;
                    case R.id.destination_exercises:
                        Intent exercisesIntent = new Intent(MainActivity.this, ExercisesActivity.class);
                        startActivity(exercisesIntent);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void addBadges() {


        if(GlobalClass.badgeList.size() == 0) {

            GlobalClass.badgeList.put("Bench Beast I", new Badge("Bench Beast I", R.drawable.winner, "Achieve a 60 kg (220 lbs) bench press."));
            GlobalClass.badgeList.put("Bench Beast II", new Badge("Bench Beast II", R.drawable.winner, "Achieve a 80 kg (220 lbs) bench press."));
            GlobalClass.badgeList.put("Bench Beast III", new Badge("Bench Beast III", R.drawable.winner, "Achieve a 100 kg (220 lbs) bench press."));
            GlobalClass.badgeList.put("Bench Beast IV", new Badge("Bench Beast IV", R.drawable.winner, "Achieve a 120 kg (220 lbs) bench press."));
            GlobalClass.badgeList.put("Bench Beast V", new Badge("Bench Beast V", R.drawable.winner, "Achieve a 140 kg (220 lbs) bench press."));
            GlobalClass.badgeList.put("Bench Beast VI", new Badge("Bench Beast VI", R.drawable.winner, "Achieve a 160 kg (220 lbs) bench press."));
            GlobalClass.badgeList.put("Squat Master", new Badge("Squat Master", R.drawable.winner, "Perform a 150 kg (330 lbs) squat with proper form."));
            GlobalClass.badgeList.put("Deadlift Dominator", new Badge("Deadlift Dominator", R.drawable.winner, "Lift 160 kg (440 lbs) in a deadlift."));
            GlobalClass.badgeList.put("Iron Abs", new Badge("Iron Abs", R.drawable.winner, "Complete 100 consecutive sit-ups."));
            GlobalClass.badgeList.put("Pull-Up Pro", new Badge("Pull-Up Pro", R.drawable.winner, "Perform 20 consecutive full-range pull-ups."));
            GlobalClass.badgeList.put("Muscle Milestone", new Badge("Muscle Milestone", R.drawable.winner, "Gain 5 kg (11 lbs) of lean muscle mass."));
            GlobalClass.badgeList.put("Push-Up Powerhouse", new Badge("Push-Up Powerhouse", R.drawable.winner, "Do 100 push-ups in a single workout session."));
            GlobalClass.badgeList.put("Plank Perfection", new Badge("Plank Perfection", R.drawable.winner, "Hold a plank for 5 minutes straight."));
            GlobalClass.badgeList.put("Speed Demon", new Badge("Speed Demon", R.drawable.winner, "Complete a 5 km (3.1 miles) run in under 20 minutes."));
            GlobalClass.badgeList.put("Dumbbell Dynamo", new Badge("Dumbbell Dynamo", R.drawable.winner, "Perform a set of 10 reps with 30 kg (66 lbs) dumbbells in each hand."));
            GlobalClass.badgeList.put("Fast and Furious", new Badge("Fast and Furious", R.drawable.winner, "Complete a 100-meter sprint in under 12 seconds."));
            GlobalClass.badgeList.put("Marathoner", new Badge("Marathoner", R.drawable.winner, "Run a half marathon (21.1 km/13.1 miles)."));
            GlobalClass.badgeList.put("Gym Bro", new Badge("Gym Bro", R.drawable.winner, "Add a friend."));
            GlobalClass.badgeList.put("Run Forrest", new Badge("Run Forrest", R.drawable.winner, "Run a marathon (42.2 km/26.2 miles)."));
            GlobalClass.badgeList.put("Dragon Warrior", new Badge("Dragon Warrior", R.drawable.winner, "Get all achievements."));
            GlobalClass.badgeList.put("Feel the Burn", new Badge("Feel the Burn", R.drawable.winner, "Perform 3 workouts in a week."));
            GlobalClass.badgeList.put("Weight of the World", new Badge("Weight of the World", R.drawable.winner, "Perform a 185kg squat."));
            GlobalClass.badgeList.put("Stairway to Heaven", new Badge("Stairway to Heaven", R.drawable.stairway_to_heaven_ic_removebg, "30 min escalator*."));
            GlobalClass.badgeList.put("Where to start?", new Badge("Where to start?", R.drawable.winner, "Open the exercise library."));
            GlobalClass.badgeList.put("Row your boat", new Badge("Row your boat", R.drawable.winner, "Use the rowing machine in your workout."));
            GlobalClass.badgeList.put("Show-off", new Badge("Show-off", R.drawable.winner, "Equip a Badge in your profile."));
            GlobalClass.badgeList.put("I could do this all day", new Badge("I could do this all day", R.drawable.winner, "Run for 45 minutes straight."));
            GlobalClass.badgeList.put("Baby’s first steps", new Badge("Baby’s first steps", R.drawable.winner, "Use the app for a week."));
            GlobalClass.badgeList.put("Picking up the pace", new Badge("Picking up the pace", R.drawable.winner, "Use the app for a month."));
            GlobalClass.badgeList.put("Gym Rat", new Badge("Gym Rat", R.drawable.winner, "Use the app for six months."));
            GlobalClass.badgeList.put("Rest week?", new Badge("Rest week?", R.drawable.winner, "Don’t perform a workout for a week."));


            //GlobalClass.saveBadgesList(this, GlobalClass.badgeList);
        }

    }



    private void addExercises() {

        String userID = snippets.getUserID();


        if(GlobalClass.getExerciseList(MainActivity.this) == null) {

                // Exercise 1: Squats
                ArrayList<String> squatsTargetedMuscles = new ArrayList<>();
                squatsTargetedMuscles.add("Quadriceps");
                squatsTargetedMuscles.add("Hamstrings");
                squatsTargetedMuscles.add("Glutes");
                squatsTargetedMuscles.add("Calves");
                Exercise squats = new Exercise("Stand with your feet shoulder-width apart, keeping your chest up and your core engaged. Lower your body by pushing your hips back and bending your knees until your thighs are parallel to the floor. Pause for a moment, then push through your heels to return to the starting position.", "Squats", squatsTargetedMuscles);
                GlobalClass.exerciseList.add(squats);

                // Exercise 2: Bench Press
                ArrayList<String> benchPressTargetedMuscles = new ArrayList<>();
                benchPressTargetedMuscles.add("Chest");
                benchPressTargetedMuscles.add("Shoulders");
                benchPressTargetedMuscles.add("Triceps");
                Exercise benchPress = new Exercise("Lie on a flat bench with your feet flat on the floor. Grasp the barbell with a grip slightly wider than shoulder-width apart. Lower the bar down to your chest, then press it back up to the starting position.", "Bench Press", benchPressTargetedMuscles);
                GlobalClass.exerciseList.add(benchPress);

                // Exercise 3: Deadlifts
                ArrayList<String> deadliftsTargetedMuscles = new ArrayList<>();
                deadliftsTargetedMuscles.add("Hamstrings");
                deadliftsTargetedMuscles.add("Glutes");
                deadliftsTargetedMuscles.add("Lower back");
                deadliftsTargetedMuscles.add("Upper back");
                Exercise deadlifts = new Exercise("Stand with your feet hip-width apart and a loaded barbell on the floor in front of you. Bend down and grip the bar with hands slightly wider than shoulder-width apart. Keeping your back straight, drive through your heels and lift the bar up, pushing your hips forward. Lower the bar back down to the floor with control.", "Deadlifts", deadliftsTargetedMuscles);
                GlobalClass.exerciseList.add(deadlifts);

                // Exercise 4: Lunges
                ArrayList<String> lungesTargetedMuscles = new ArrayList<>();
                lungesTargetedMuscles.add("Quadriceps");
                lungesTargetedMuscles.add("Hamstrings");
                lungesTargetedMuscles.add("Glutes");
                lungesTargetedMuscles.add("Calves");
                Exercise lunges = new Exercise("Stand with your feet hip-width apart. Take a step forward with your right leg and lower your body until your right knee is bent at a 90-degree angle. Push through your right heel to return to the starting position. Repeat with the left leg.", "Lunges", lungesTargetedMuscles);
                GlobalClass.exerciseList.add(lunges);

                // Exercise 5: Shoulder Press
                ArrayList<String> shoulderPressTargetedMuscles = new ArrayList<>();
                shoulderPressTargetedMuscles.add("Shoulders");
                shoulderPressTargetedMuscles.add("Triceps");
                shoulderPressTargetedMuscles.add("Upper back");
                Exercise shoulderPress = new Exercise("Sit on a bench with a dumbbell in each hand, palms facing forward. Press the dumbbells up above your head until your arms are fully extended. Lower the dumbbells back down to the starting position.", "Shoulder Press", shoulderPressTargetedMuscles);
                GlobalClass.exerciseList.add(shoulderPress);

                // Exercise 6: Pull-ups
                ArrayList<String> pullUpsTargetedMuscles = new ArrayList<>();
                pullUpsTargetedMuscles.add("Back");
                pullUpsTargetedMuscles.add("Biceps");
                pullUpsTargetedMuscles.add("Shoulders");
                Exercise pullUps = new Exercise("Hang from a pull-up bar with your palms facing away from you. Pull your body up towards the bar until your chin is above it. Lower your body back down to the starting position.", "Pull-ups", pullUpsTargetedMuscles);
                GlobalClass.exerciseList.add(pullUps);

                // Exercise 7: Dumbbell Rows
                ArrayList<String> dumbbellRowsTargetedMuscles = new ArrayList<>();
                dumbbellRowsTargetedMuscles.add("Back");
                dumbbellRowsTargetedMuscles.add("Biceps");
                dumbbellRowsTargetedMuscles.add("Shoulders");
                Exercise dumbbellRows = new Exercise("Place your left knee and left hand on a bench, and hold a dumbbell in your right hand. Keep your back straight and pull the dumbbell up towards your chest, squeezing your shoulder blades together. Lower the dumbbell back down and repeat on the other side.", "Dumbbell Rows", dumbbellRowsTargetedMuscles);
                GlobalClass.exerciseList.add(dumbbellRows);

                // Exercise 8: Push-ups
                ArrayList<String> pushUpsTargetedMuscles = new ArrayList<>();
                pushUpsTargetedMuscles.add("Chest");
                pushUpsTargetedMuscles.add("Triceps");
                pushUpsTargetedMuscles.add("Shoulders");
                Exercise pushUps = new Exercise("Start in a high plank position with your hands shoulder-width apart. Lower your body until your chest nearly touches the floor, then push yourself back up to the starting position.", "Push-ups", pushUpsTargetedMuscles);
                GlobalClass.exerciseList.add(pushUps);

                // Exercise 9: Bicep Curls
                ArrayList<String> bicepCurlsTargetedMuscles = new ArrayList<>();
                bicepCurlsTargetedMuscles.add("Biceps");
                Exercise bicepCurls = new Exercise("Stand with a dumbbell in each hand, palms facing forward. Keeping your upper arms stationary, curl the dumbbells up towards your shoulders. Lower the dumbbells back down to the starting position.", "Bicep Curls", bicepCurlsTargetedMuscles);
                GlobalClass.exerciseList.add(bicepCurls);

                // Exercise 10: Tricep Dips
                ArrayList<String> tricepDipsTargetedMuscles = new ArrayList<>();
                tricepDipsTargetedMuscles.add("Triceps");
                tricepDipsTargetedMuscles.add("Chest");
                tricepDipsTargetedMuscles.add("Shoulders");
                Exercise tricepDips = new Exercise("Sit on the edge of a bench with your hands next to your thighs, fingers pointing forward. Slide your hips off the bench and lower your body by bending your elbows. Push yourself back up to the starting position.", "Tricep Dips", tricepDipsTargetedMuscles);
                GlobalClass.exerciseList.add(tricepDips);

                // Exercise 11: Leg Press
                ArrayList<String> legPressTargetedMuscles = new ArrayList<>();
                legPressTargetedMuscles.add("Quadriceps");
                legPressTargetedMuscles.add("Hamstrings");
                legPressTargetedMuscles.add("Glutes");
                legPressTargetedMuscles.add("Calves");
                Exercise legPress = new Exercise("Sit on a leg press machine with your feet on the footplate. Push the footplate away from your body by extending your knees, then return to the starting position by bending your knees.", "Leg Press", legPressTargetedMuscles);
                GlobalClass.exerciseList.add(legPress);

                // Exercise 12: Calf Raises
                ArrayList<String> calfRaisesTargetedMuscles = new ArrayList<>();
                calfRaisesTargetedMuscles.add("Calves");
                Exercise calfRaises = new Exercise("Stand on the edge of a step or a platform with your heels hanging off. Rise up onto your toes, then lower your heels back down below the platform.", "Calf Raises", calfRaisesTargetedMuscles);
                GlobalClass.exerciseList.add(calfRaises);

                // Exercise 13: Russian Twists
                ArrayList<String> russianTwistsTargetedMuscles = new ArrayList<>();
                russianTwistsTargetedMuscles.add("Abdominals");
                russianTwistsTargetedMuscles.add("Obliques");
                Exercise russianTwists = new Exercise("Sit on the floor with your knees bent and feet lifted off the ground. Hold a weight or medicine ball in front of your chest. Twist your torso to the right, then to the left, while keeping your feet off the ground.", "Russian Twists", russianTwistsTargetedMuscles);
                GlobalClass.exerciseList.add(russianTwists);

                // Exercise 14: Plank
                ArrayList<String> plankTargetedMuscles = new ArrayList<>();
                plankTargetedMuscles.add("Abdominals");
                plankTargetedMuscles.add("Back");
                plankTargetedMuscles.add("Shoulders");
                Exercise plank = new Exercise("Start in a high plank position with your hands directly under your shoulders. Engage your core and hold the position for a specified amount of time.", "Plank", plankTargetedMuscles);
                GlobalClass.exerciseList.add(plank);

                // Exercise 15: Mountain Climbers
                ArrayList<String> mountainClimbersTargetedMuscles = new ArrayList<>();
                mountainClimbersTargetedMuscles.add("Abdominals");
                mountainClimbersTargetedMuscles.add("Shoulders");
                mountainClimbersTargetedMuscles.add("Quadriceps");
                Exercise mountainClimbers = new Exercise("Start in a high plank position. Drive one knee towards your chest, then quickly switch legs, as if you're running in place. Continue alternating legs at a fast pace.", "Mountain Climbers", mountainClimbersTargetedMuscles);
                GlobalClass.exerciseList.add(mountainClimbers);

                // Exercise 16: Lat Pulldowns
                ArrayList<String> latPulldownsTargetedMuscles = new ArrayList<>();
                latPulldownsTargetedMuscles.add("Back");
                latPulldownsTargetedMuscles.add("Biceps");
                latPulldownsTargetedMuscles.add("Shoulders");
                Exercise latPulldowns = new Exercise("Sit at a lat pulldown machine and grip the bar with your hands wider than shoulder-width apart. Pull the bar down towards your chest, then slowly return to the starting position.", "Lat Pulldowns", latPulldownsTargetedMuscles);
                GlobalClass.exerciseList.add(latPulldowns);

                // Exercise 17: Leg Extensions
                ArrayList<String> legExtensionsTargetedMuscles = new ArrayList<>();
                legExtensionsTargetedMuscles.add("Quadriceps");
                Exercise legExtensions = new Exercise("Sit on a leg extension machine with your legs positioned under the padded bar. Extend your legs and straighten your knees, then return to the starting position.", "Leg Extensions", legExtensionsTargetedMuscles);
                GlobalClass.exerciseList.add(legExtensions);

                // Exercise 18: Hamstring Curls
                ArrayList<String> hamstringCurlsTargetedMuscles = new ArrayList<>();
                hamstringCurlsTargetedMuscles.add("Hamstrings");
                Exercise hamstringCurls = new Exercise("Lie face down on a hamstring curl machine with your heels hooked under the padded lever. Curl your legs up towards your glutes, then slowly lower them back down.", "Hamstring Curls", hamstringCurlsTargetedMuscles);
                GlobalClass.exerciseList.add(hamstringCurls);

                // Exercise 19: Barbell Rows
                ArrayList<String> barbellRowsTargetedMuscles = new ArrayList<>();
                barbellRowsTargetedMuscles.add("Back");
                barbellRowsTargetedMuscles.add("Biceps");
                barbellRowsTargetedMuscles.add("Shoulders");
                Exercise barbellRows = new Exercise("Bend at the hips with a barbell in front of you, knees slightly bent. Keeping your back straight, pull the barbell up towards your lower chest, squeezing your shoulder blades together. Lower the barbell back down and repeat.", "Barbell Rows", barbellRowsTargetedMuscles);
                GlobalClass.exerciseList.add(barbellRows);

                // Exercise 20: Incline Bench Press
                ArrayList<String> inclineBenchPressTargetedMuscles = new ArrayList<>();
                inclineBenchPressTargetedMuscles.add("Upper Chest");
                inclineBenchPressTargetedMuscles.add("Shoulders");
                inclineBenchPressTargetedMuscles.add("Triceps");
                Exercise inclineBenchPress = new Exercise("Lie on an incline bench with your feet flat on the floor. Grasp the barbell with a grip slightly wider than shoulder-width apart. Lower the bar down to your upper chest, then press it back up to the starting position.", "Incline Bench Press", inclineBenchPressTargetedMuscles);
                GlobalClass.exerciseList.add(inclineBenchPress);

                // Exercise 21: Dumbbell Flyes
                ArrayList<String> dumbbellFlyesTargetedMuscles = new ArrayList<>();
                dumbbellFlyesTargetedMuscles.add("Chest");
                dumbbellFlyesTargetedMuscles.add("Shoulders");
                Exercise dumbbellFlyes = new Exercise("Lie on a flat bench with a dumbbell in each hand, palms facing each other. Lower the dumbbells out to the sides in a wide arc until you feel a stretch in your chest. Bring the dumbbells back up in the same wide arc.", "Dumbbell Flyes", dumbbellFlyesTargetedMuscles);
                GlobalClass.exerciseList.add(dumbbellFlyes);

                // Exercise 22: Seated Dumbbell Shoulder Press
                ArrayList<String> seatedDumbbellShoulderPressTargetedMuscles = new ArrayList<>();
                seatedDumbbellShoulderPressTargetedMuscles.add("Shoulders");
                seatedDumbbellShoulderPressTargetedMuscles.add("Triceps");
                seatedDumbbellShoulderPressTargetedMuscles.add("Upper back");
                Exercise seatedDumbbellShoulderPress = new Exercise("Sit on a bench with a dumbbell in each hand, palms facing forward. Press the dumbbells up above your head until your arms are fully extended. Lower the dumbbells back down to the starting position.", "Seated Dumbbell Shoulder Press", seatedDumbbellShoulderPressTargetedMuscles);
                GlobalClass.exerciseList.add(seatedDumbbellShoulderPress);

                // Exercise 23: Hammer Curls
                ArrayList<String> hammerCurlsTargetedMuscles = new ArrayList<>();
                hammerCurlsTargetedMuscles.add("Biceps");
                hammerCurlsTargetedMuscles.add("Forearms");
                Exercise hammerCurls = new Exercise("Stand with a dumbbell in each hand, palms facing your body. Keeping your upper arms stationary, curl the dumbbells up towards your shoulders. Lower the dumbbells back down to the starting position.", "Hammer Curls", hammerCurlsTargetedMuscles);
                GlobalClass.exerciseList.add(hammerCurls);

                // Exercise 24: Skull Crushers
                ArrayList<String> skullCrushersTargetedMuscles = new ArrayList<>();
                skullCrushersTargetedMuscles.add("Triceps");
                skullCrushersTargetedMuscles.add("Shoulders");
                skullCrushersTargetedMuscles.add("Chest");
                Exercise skullCrushers = new Exercise("Lie on a flat bench with a barbell or dumbbells in your hands. Extend your arms straight up towards the ceiling. Bend your elbows and lower the weight(s) down towards your forehead, then extend your arms back up.", "Skull Crushers", skullCrushersTargetedMuscles);
                GlobalClass.exerciseList.add(skullCrushers);

                // Exercise 25: Lateral Raises
                ArrayList<String> lateralRaisesTargetedMuscles = new ArrayList<>();
                lateralRaisesTargetedMuscles.add("Shoulders");
                lateralRaisesTargetedMuscles.add("Upper back");
                Exercise lateralRaises = new Exercise("Stand with a dumbbell in each hand, palms facing your body. Raise your arms out to the sides until they're parallel to the floor, then lower them back down.", "Lateral Raises", lateralRaisesTargetedMuscles);
                GlobalClass.exerciseList.add(lateralRaises);

                // Exercise 26: Leg Curls
                ArrayList<String> legCurlsTargetedMuscles = new ArrayList<>();
                legCurlsTargetedMuscles.add("Hamstrings");
                legCurlsTargetedMuscles.add("Glutes");
                Exercise legCurls = new Exercise("Lie face down on a leg curl machine with your heels hooked under the padded lever. Curl your legs up towards your glutes, then slowly lower them back down.", "Leg Curls", legCurlsTargetedMuscles);
                GlobalClass.exerciseList.add(legCurls);

                // Exercise 27: Dumbbell Lunges
                ArrayList<String> dumbbellLungesTargetedMuscles = new ArrayList<>();
                dumbbellLungesTargetedMuscles.add("Quadriceps");
                dumbbellLungesTargetedMuscles.add("Hamstrings");
                dumbbellLungesTargetedMuscles.add("Glutes");
                dumbbellLungesTargetedMuscles.add("Calves");
                Exercise dumbbellLunges = new Exercise("Stand with your feet hip-width apart and hold a dumbbell in each hand. Take a step forward with your right leg and lower your body until your right knee is bent at a 90-degree angle. Push through your right heel to return to the starting position. Repeat with the left leg.", "Dumbbell Lunges", dumbbellLungesTargetedMuscles);
                GlobalClass.exerciseList.add(dumbbellLunges);

                // Exercise 28: Arnold Press
                ArrayList<String> arnoldPressTargetedMuscles = new ArrayList<>();
                arnoldPressTargetedMuscles.add("Shoulders");
                arnoldPressTargetedMuscles.add("Triceps");
                arnoldPressTargetedMuscles.add("Upper back");
                Exercise arnoldPress = new Exercise("Sit on a bench with a dumbbell in each hand, palms facing towards your body. Start with the dumbbells at shoulder level, palms facing towards your body. Press the dumbbells up above your head, rotating your palms to face forward. Lower the dumbbells back down to the starting position, rotating your palms back towards your body.", "Arnold Press", arnoldPressTargetedMuscles);
                GlobalClass.exerciseList.add(arnoldPress);

                // Exercise 29: Cable Crossover
                ArrayList<String> cableCrossoverTargetedMuscles = new ArrayList<>();
                cableCrossoverTargetedMuscles.add("Chest");
                cableCrossoverTargetedMuscles.add("Shoulders");
                Exercise cableCrossover = new Exercise("Stand in the middle of a cable machine with the pulleys set to the highest position. Hold one handle in each hand and step forward with one foot. Lean slightly forward and bring your arms down and across your body in a sweeping motion, stopping when your hands are in line with your chest. Return to the starting position.", "Cable Crossover", cableCrossoverTargetedMuscles);
                GlobalClass.exerciseList.add(cableCrossover);

                // Exercise 30: Reverse Lunges
                ArrayList<String> reverseLungesTargetedMuscles = new ArrayList<>();
                reverseLungesTargetedMuscles.add("Quadriceps");
                reverseLungesTargetedMuscles.add("Hamstrings");
                reverseLungesTargetedMuscles.add("Glutes");
                reverseLungesTargetedMuscles.add("Calves");
                Exercise reverseLunges = new Exercise("Stand with your feet hip-width apart. Take a step back with your right leg and lower your body until your right knee is bent at a 90-degree angle. Push through your left heel to return to the starting position. Repeat with the left leg.", "Reverse Lunges", reverseLungesTargetedMuscles);
                GlobalClass.exerciseList.add(reverseLunges);

                // Exercise 31: Bicycle Crunches
                ArrayList<String> bicycleCrunchesTargetedMuscles = new ArrayList<>();
                bicycleCrunchesTargetedMuscles.add("Abdominals");
                bicycleCrunchesTargetedMuscles.add("Obliques");
                Exercise bicycleCrunches = new Exercise("Lie on your back with your hands behind your head and your legs lifted off the ground. Bring your right elbow towards your left knee while straightening your right leg. Alternate sides in a pedaling motion, as if you're riding a bicycle.", "Bicycle Crunches", bicycleCrunchesTargetedMuscles);
                GlobalClass.exerciseList.add(bicycleCrunches);

                // Exercise 32: Seated Cable Rows
                ArrayList<String> seatedCableRowsTargetedMuscles = new ArrayList<>();
                seatedCableRowsTargetedMuscles.add("Back");
                seatedCableRowsTargetedMuscles.add("Biceps");
                seatedCableRowsTargetedMuscles.add("Shoulders");
                Exercise seatedCableRows = new Exercise("Sit at a cable row machine with your knees slightly bent and your feet firmly on the footplates. Grab the handles with your palms facing each other. Keeping your back straight, pull the handles towards your body, squeezing your shoulder blades together. Return to the starting position.", "Seated Cable Rows", seatedCableRowsTargetedMuscles);
                GlobalClass.exerciseList.add(seatedCableRows);

                // Exercise 33: Weighted Step-ups
                ArrayList<String> weightedStepUpsTargetedMuscles = new ArrayList<>();
                weightedStepUpsTargetedMuscles.add("Quadriceps");
                weightedStepUpsTargetedMuscles.add("Glutes");
                weightedStepUpsTargetedMuscles.add("Hamstrings");
                Exercise weightedStepUps = new Exercise("Stand in front of a step or platform with a dumbbell in each hand. Step up onto the platform with your right foot, then bring your left foot up to meet it. Step back down with your right foot, followed by your left foot. Repeat on the other side.", "Weighted Step-ups", weightedStepUpsTargetedMuscles);
                GlobalClass.exerciseList.add(weightedStepUps);

                // Exercise 34: Standing Calf Raises
                ArrayList<String> standingCalfRaisesTargetedMuscles = new ArrayList<>();
                standingCalfRaisesTargetedMuscles.add("Calves");
                Exercise standingCalfRaises = new Exercise("Stand with the balls of your feet on an elevated surface, such as a step or a block. Lower your heels as far as possible, then rise up onto your toes. Lower your heels back down and repeat.", "Standing Calf Raises", standingCalfRaisesTargetedMuscles);
                GlobalClass.exerciseList.add(standingCalfRaises);

                // Exercise 35: Decline Bench Press
                ArrayList<String> declineBenchPressTargetedMuscles = new ArrayList<>();
                declineBenchPressTargetedMuscles.add("Lower Chest");
                declineBenchPressTargetedMuscles.add("Triceps");
                declineBenchPressTargetedMuscles.add("Shoulders");
                Exercise declineBenchPress = new Exercise("Lie on a decline bench with your feet secured at the top. Grasp the barbell with a grip slightly wider than shoulder-width apart. Lower the bar down to your lower chest, then press it back up to the starting position.", "Decline Bench Press", declineBenchPressTargetedMuscles);
                GlobalClass.exerciseList.add(declineBenchPress);

                ArrayList<String> overheadPressTargetedMuscles = new ArrayList<>();
                overheadPressTargetedMuscles.add("Shoulders");
                overheadPressTargetedMuscles.add("Triceps");
                Exercise overheadPress = new Exercise("Stand with your feet shoulder-width apart and hold a barbell at shoulder level. Press the barbell overhead until your arms are fully extended, then lower it back down to the starting position.", "Overhead Press", overheadPressTargetedMuscles);
                GlobalClass.exerciseList.add(overheadPress);

// Exercise 37: Romanian Deadlifts
                ArrayList<String> romanianDeadliftsTargetedMuscles = new ArrayList<>();
                romanianDeadliftsTargetedMuscles.add("Hamstrings");
                romanianDeadliftsTargetedMuscles.add("Glutes");
                romanianDeadliftsTargetedMuscles.add("Lower back");
                romanianDeadliftsTargetedMuscles.add("Upper back");
                Exercise romanianDeadlifts = new Exercise("Stand with your feet hip-width apart and a loaded barbell in front of you. Hinge at the hips, keeping your back straight, and lower the barbell down your legs. Push your hips forward to return to the starting position.", "Romanian Deadlifts", romanianDeadliftsTargetedMuscles);
                GlobalClass.exerciseList.add(romanianDeadlifts);

// Exercise 38: Dumbbell Bench Press
                ArrayList<String> dumbbellBenchPressTargetedMuscles = new ArrayList<>();
                dumbbellBenchPressTargetedMuscles.add("Chest");
                dumbbellBenchPressTargetedMuscles.add("Shoulders");
                dumbbellBenchPressTargetedMuscles.add("Triceps");
                Exercise dumbbellBenchPress = new Exercise("Lie on a flat bench with a dumbbell in each hand, palms facing forward. Lower the dumbbells down to your chest, then press them back up to the starting position.", "Dumbbell Bench Press", dumbbellBenchPressTargetedMuscles);
                GlobalClass.exerciseList.add(dumbbellBenchPress);

// Exercise 39: Barbell Squats
                ArrayList<String> barbellSquatsTargetedMuscles = new ArrayList<>();
                barbellSquatsTargetedMuscles.add("Quadriceps");
                barbellSquatsTargetedMuscles.add("Hamstrings");
                barbellSquatsTargetedMuscles.add("Glutes");
                barbellSquatsTargetedMuscles.add("Calves");
                Exercise barbellSquats = new Exercise("Stand with your feet shoulder-width apart, keeping your chest up and your core engaged. Lower your body by pushing your hips back and bending your knees until your thighs are parallel to the floor. Pause for a moment, then push through your heels to return to the starting position.", "Barbell Squats", barbellSquatsTargetedMuscles);
                GlobalClass.exerciseList.add(barbellSquats);

                // Exercise 40: Dumbbell Shoulder Press
                ArrayList<String> dumbbellShoulderPressTargetedMuscles = new ArrayList<>();
                dumbbellShoulderPressTargetedMuscles.add("Shoulders");
                dumbbellShoulderPressTargetedMuscles.add("Triceps");
                dumbbellShoulderPressTargetedMuscles.add("Upper back");
                Exercise dumbbellShoulderPress = new Exercise("Sit on a bench with a dumbbell in each hand, palms facing forward. Press the dumbbells up above your head until your arms are fully extended. Lower the dumbbells back down to the starting position.", "Dumbbell Shoulder Press", dumbbellShoulderPressTargetedMuscles);
                GlobalClass.exerciseList.add(dumbbellShoulderPress);

// Exercise 41: Barbell Lunges
                ArrayList<String> barbellLungesTargetedMuscles = new ArrayList<>();
                barbellLungesTargetedMuscles.add("Quadriceps");
                barbellLungesTargetedMuscles.add("Hamstrings");
                barbellLungesTargetedMuscles.add("Glutes");
                barbellLungesTargetedMuscles.add("Calves");
                Exercise barbellLunges = new Exercise("Stand with your feet hip-width apart and a barbell across your upper back. Take a step forward with your right leg and lower your body until your right knee is bent at a 90-degree angle. Push through your right heel to return to the starting position. Repeat with the left leg.", "Barbell Lunges", barbellLungesTargetedMuscles);
                GlobalClass.exerciseList.add(barbellLunges);

// Exercise 42: Seated Rows
                ArrayList<String> seatedRowsTargetedMuscles = new ArrayList<>();
                seatedRowsTargetedMuscles.add("Back");
                seatedRowsTargetedMuscles.add("Biceps");
                seatedRowsTargetedMuscles.add("Shoulders");
                Exercise seatedRows = new Exercise("Sit at a row machine with your knees slightly bent and your feet firmly on the footplates. Grab the handles with your palms facing each other. Keeping your back straight, pull the handles towards your body, squeezing your shoulder blades together. Return to the starting position.", "Seated Rows", seatedRowsTargetedMuscles);
                GlobalClass.exerciseList.add(seatedRows);

// Exercise 43: Barbell Bicep Curls
                ArrayList<String> barbellBicepCurlsTargetedMuscles = new ArrayList<>();
                barbellBicepCurlsTargetedMuscles.add("Biceps");
                Exercise barbellBicepCurls = new Exercise("Stand with a barbell in your hands, palms facing forward and hands shoulder-width apart. Keeping your upper arms stationary, curl the barbell up towards your shoulders. Lower the barbell back down to the starting position.", "Barbell Bicep Curls", barbellBicepCurlsTargetedMuscles);
                GlobalClass.exerciseList.add(barbellBicepCurls);



                GlobalClass.musclesList.add("Quadriceps");
                GlobalClass.musclesList.add("Hamstrings");
                GlobalClass.musclesList.add("Glutes");
                GlobalClass.musclesList.add("Calves");
                GlobalClass.musclesList.add("Chest");
                GlobalClass.musclesList.add("Shoulders");
                GlobalClass.musclesList.add("Triceps");
                GlobalClass.musclesList.add("Lower back");
                GlobalClass.musclesList.add("Upper back");
                GlobalClass.musclesList.add("Back");
                GlobalClass.musclesList.add("Biceps");
                GlobalClass.musclesList.add("Abdominals");
                GlobalClass.musclesList.add("Obliques");
                GlobalClass.musclesList.add("Forearms");
                GlobalClass.musclesList.add("Upper Chest");
                GlobalClass.musclesList.add("Lower Chest");


                GlobalClass.saveExerciseList(this, GlobalClass.exerciseList);



            }




        }
    }



