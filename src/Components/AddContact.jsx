import axios from "axios";
import { useParams } from "react-router-dom";
import PropTypes from "prop-types";
import { useState } from "react";
import '../Pages/add-contact.css'
import {RxCross2} from "react-icons/rx";

// Prop types
AddContact.propTypes = {
    name: PropTypes.string.isRequired,
    email: PropTypes.oneOfType([PropTypes.string, PropTypes.arrayOf(PropTypes.string)]).isRequired,
    edit: PropTypes.bool.isRequired,
};

// Component
function AddContact(props) {
    const { user } = useParams();
    const [email, setEmail] = useState(Array.isArray(props.email) ? props.email.join(" ") : props.email);
    const [name, setName] = useState(props.name);

    async function addContact() {
        const emailArray = email.split(" ");
        let params;

        if (props.edit) {
            params = {
                oldContact: {
                    name: props.name,
                    userEmails: Array.isArray(props.email) ? props.email : [props.email]
                },
                newContact: {
                    name,
                    userEmails: emailArray
                }
            };
            console.log(params);
            await axios.post(`http://localhost:8080/api/users/editContact/${user}`, params);
        } else {
            params = {
                name,
                userEmails: emailArray
            };
            console.log(params);
            await axios.post(`http://localhost:8080/api/users/addContact/${user}`, params);
        }
    }


    return (
        <div className="contact-container">
            {/* eslint-disable-next-line react/prop-types */}
            <button className={"btn btn-outline-danger btn-sm close"} onClick={props.onClick}>
                <RxCross2/>
            </button>
            <div>
                <label htmlFor={"name"}>Name: </label>
                <input type={"text"} id={"name"} value={name} onChange={(e) => setName(e.target.value)}/>
            </div>
            <div>
                <label htmlFor={"email"}>Email: </label>
                <input type={"email"} id={"email"} value={email} onChange={(e) => setEmail(e.target.value)}/>
            </div>
            <button className={"btn btn-primary"} onClick={addContact}>{props.edit ? "Edit" : "Add Contact"}</button>
        </div>
    );
}

export default AddContact;
