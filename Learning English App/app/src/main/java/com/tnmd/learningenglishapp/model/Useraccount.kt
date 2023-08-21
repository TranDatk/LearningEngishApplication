import com.fasterxml.jackson.annotation.JsonIgnore
import com.tnmd.learningenglishapp.model.Learner

class Useraccount {
    private lateinit var id : String
    private lateinit var email : String
    private lateinit var password : String
    private lateinit var avatar : String
    private lateinit var status : String

    //@get:JsonIgnore
    private lateinit var learner: Learner

    // Getter and Setter for id
    fun getId(): String {
        return id
    }

    fun setId(newId: String) {
        id = newId
    }

    // Getter and Setter for email
    fun getEmail(): String {
        return email
    }

    fun setEmail(newEmail: String) {
        email = newEmail
    }

    // Getter and Setter for password
    fun getPassword(): String {
        return password
    }

    fun setPassword(newPassword: String) {
        password = newPassword
    }

    // Getter and Setter for avatar
    fun getAvatar(): String {
        return avatar
    }

    fun setAvatar(newAvatar: String) {
        avatar = newAvatar
    }

    // Getter and Setter for status
    fun getStatus(): String {
        return status
    }

    fun setStatus(newStatus: String) {
        status = newStatus
    }

    fun getLearner(): Learner {
        return learner
    }

    fun setLearner(newLearner: Learner) {
        learner = newLearner
    }
}