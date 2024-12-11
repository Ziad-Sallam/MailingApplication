import PropTypes from 'prop-types';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { useState } from 'react';

MailBox.propTypes = {
    userEmail: PropTypes.string,
};

function MailBox() {
    const [to, setTo] = useState('');
    const [body, setBody] = useState('');
    const [subject, setSubject] = useState('');
    const [priority, setPriority] = useState(2);

    function handleEntryChange(e) {
        const { id, value } = e.target;
        if (id === 'to') {
            setTo(value);
        } else if (id === 'subject') {
            setSubject(value);
        } else if (id === 'body') {
            setBody(value);
        } else if(id === 'pri'){
            setPriority(value);
        }
    }

    const params = useParams();
    console.log(params);

    async function sendMail(e) {

        const param = {
            sender: params.user,
            receivers: to.split(' '),
            subject: subject,
            body: body,
            priority: priority,
        };

        console.log(param.receiver);
        try {
            await axios.post('http://localhost:8080/api/users/send', param);
        } catch (error) {
            console.error('Error posting data:', error);
        }
    }

    return (
        <div className="d-flex flex-column mb-3 mail-box">
            <form className="form-control" onSubmit={sendMail}>
                <table className="table table-striped">
                    <tbody>
                    <tr>
                        <td><label htmlFor="sender">From: </label></td>
                        <td><h6>{params.user}</h6></td>
                    </tr>
                    <tr>
                        <td><label htmlFor="receiver">To: </label></td>
                        <td>
                            <input
                                id="to"
                                className="col-12"
                                type="text"
                                name="receiver"
                                placeholder="to"
                                onChange={handleEntryChange}
                            /><br />
                        </td>
                    </tr>
                    <tr>
                        <td><label htmlFor="subject">Subject: </label></td>
                        <td>
                            <input
                                id="subject"
                                className="col-12"
                                type="text"
                                name="subject"
                                placeholder="subject"
                                onChange={handleEntryChange}
                            /><br />
                        </td>
                    </tr>
                    <tr>
                        <td><label htmlFor="body">Body: </label></td>
                        <td>
                                <textarea
                                    id="body"
                                    className="col-12"
                                    name="message"
                                    placeholder="message"
                                    onChange={handleEntryChange}
                                />
                        </td>
                    </tr>
                    <tr>
                        <td><label htmlFor="priority">Priority: </label></td>
                        <td><input id= "pri" className="input input-number" type="number" min="0" max="5" onChange={handleEntryChange}/></td>
                    </tr>
                    <tr>
                        <td><label htmlFor="attachment">Attachment: </label></td>
                        <td><input className="input input-file" type="file" /></td>
                    </tr>
                    </tbody>
                </table>
                <button
                    className="btn btn-primary btn-lg btn-block position-relative"
                    style={{ marginLeft: '87%', marginRight: '20px', marginTop: '-9px', marginBottom: '10px' }}
                    type="submit"
                    disabled={!to.trim() || !body.trim() || !subject.trim()}
                >
                    Submit
                </button>
            </form>
        </div>
    );
}

export default MailBox;
