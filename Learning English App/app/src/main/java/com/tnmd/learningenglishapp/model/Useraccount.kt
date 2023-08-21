import com.fasterxml.jackson.annotation.JsonIgnore
import com.tnmd.learningenglishapp.model.Learner

class Useraccount {
    private var id : String
    private var email : String
    private var password : String
    private var avatar : String
    private var status : Boolean = false

    //@get:JsonIgnore
    private var learnerId: String

    constructor(
        id: String,
        email: String,
        password: String,
        avatar: String,
        status: Boolean,
        learnerId: String
    ) {
        this.id = id
        this.email = email
        this.password = password
        this.avatar = avatar
        this.status = status
        this.learnerId = learnerId
    }

    constructor(
    ) {
        this.id = ""
        this.email = ""
        this.password = ""
        this.avatar = ""
        this.status = false
        this.learnerId = ""
    }

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
    fun getStatus(): Boolean {
        return status
    }

    fun setStatus(newStatus: Boolean) {
        status = newStatus
    }

    fun getLearner(): String {
        return learnerId
    }

    fun setLearner(newLearner: String) {
        learnerId = newLearner
    }
}