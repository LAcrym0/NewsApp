package esgi.com.newsapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import esgi.com.newsapp.R;
import esgi.com.newsapp.model.Auth;
import esgi.com.newsapp.model.Comment;
import esgi.com.newsapp.model.News;
import esgi.com.newsapp.model.Post;
import esgi.com.newsapp.model.Topic;
import esgi.com.newsapp.model.User;
import esgi.com.newsapp.network.ApiResult;
import esgi.com.newsapp.network.RetrofitSession;
import esgi.com.newsapp.utils.DateConverter;
import esgi.com.newsapp.utils.PreferencesHelper;

public class TestsActivity extends AppCompatActivity {

    private Topic topic = new Topic("Premier essai", "Tentative de la fatigue", DateConverter.getCurrentFormattedDate());
    private Post post = new Post("Premier post du premier essai", "En effet, la fatigue est présente", "", DateConverter.getCurrentFormattedDate());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final User user = new User("test127@digipolitan.com", "testcréationtest", "UnMarabout", "BarbuBreton");
        Auth auth = new Auth(user.getEmail(), user.getPassword());
        System.out.println("Launching");

        /*RetrofitSession.getInstance().getUserService().createAccount(user, new ApiResult<Void>() {
            @Override
            public void success(Void res) {
                login(user);
            }

            @Override
            public void error(int code, String message) {
                System.out.println(message);
            }
        });*/
        login(auth);
    }

    private void login(Auth auth) {
        RetrofitSession.getInstance().getUserService().login(auth, new ApiResult<String>() {
            @Override
            public void success(String token) {
                System.out.println(token);
                //getUserInfo(); //OK
                //getUsersList(); //OK
                //createTopic(topic);
                getTopics();
                /*getPosts();
                getNews();
                getComments();*/
            }

            @Override
            public void error(int code, String message) {
                System.out.println(message);
            }
        });
    }

    private void getUserInfo() {
        RetrofitSession.getInstance().getUserService().getMyInformation(new ApiResult<User>() {
            @Override
            public void success(User user) {
                //System.out.println(token);
                user.setFirstname("Toto le testeur");
                editUser(user);
            }

            @Override
            public void error(int code, String message) {
                System.out.println(message);
            }
        });
    }

    private void getUsersList() {
        RetrofitSession.getInstance().getUserService().getUsersList(new ApiResult<List<User>>() {
            @Override
            public void success(List<User> users) {
                //System.out.println(token);
                /*for (User user : users)
                    System.out.println(user.getEmail());*///verification OK
            }

            @Override
            public void error(int code, String message) {
                System.out.println(message);
            }
        });
    }

    private void editUser(User user) {
        RetrofitSession.getInstance().getUserService().editUser(user, new ApiResult<User>() {
            @Override
            public void success(User editedUser) {
                System.out.println(editedUser.getFirstname());
            }

            @Override
            public void error(int code, String message) {
                System.out.println(message);
            }
        });
    }

    private void getComments() {
        RetrofitSession.getInstance().getCommentService().getCommentsList(new ApiResult<List<Comment>>() {
            @Override
            public void success(List<Comment> res) {

            }

            @Override
            public void error(int code, String message) {

            }
        });
    }

    private void getNews() {
        RetrofitSession.getInstance().getNewsService().getNewsList(new ApiResult<List<News>>() {
            @Override
            public void success(List<News> res) {

            }

            @Override
            public void error(int code, String message) {

            }
        });
    }

    private void getPosts() {
        RetrofitSession.getInstance().getPostService().getPostList(new ApiResult<List<Post>>() {
            @Override
            public void success(List<Post> res) {
                for (Post post : res) {
                    System.out.println(post.getContent());
                    System.out.println(post.getAuthor());
                    System.out.println(post.getId());
                    System.out.println(post.getDate());
                    System.out.println(post.getTitle());
                    System.out.println(post.getTopic());
                    System.out.println("------");
                }
                getPost(res.get(res.size() - 1).getId());
            }

            @Override
            public void error(int code, String message) {

            }
        });
    }

    private void getTopics() {
        RetrofitSession.getInstance().getTopicService().getTopicList(new ApiResult<List<Topic>>() {
            @Override
            public void success(List<Topic> res) {
                post.setTopic(res.get(res.size() - 1).getId());
                createPost(post);
            }

            @Override
            public void error(int code, String message) {

            }
        });
    }

    private void createTopic(Topic topic) {
        System.out.println("DATE : " + topic.getDate());
        RetrofitSession.getInstance().getTopicService().createTopic(topic, new ApiResult<Void>() {
            @Override
            public void success(Void res) {
                getTopics();
            }

            @Override
            public void error(int code, String message) {
                System.out.println("Error while creating the topic");
            }
        });
    }

    private void createPost(Post post) {
        RetrofitSession.getInstance().getPostService().createPost(post, new ApiResult<Void>() {
            @Override
            public void success(Void res) {
                getPosts();
            }

            @Override
            public void error(int code, String message) {
                System.out.println("Error while creating the topic");
            }
        });
    }

    private void getPost(String id) {
        RetrofitSession.getInstance().getPostService().getPost(id, new ApiResult<Post>() {
            @Override
            public void success(Post post) {
                System.out.println("------");
                System.out.println(post.getContent());
                System.out.println(post.getAuthor());
                System.out.println(post.getId());
                System.out.println(post.getDate());
                System.out.println(post.getTitle());
                System.out.println(post.getTopic());
                System.out.println("------");
            }

            @Override
            public void error(int code, String message) {
                System.out.println("Error while creating the topic");
            }
        });
    }
}
