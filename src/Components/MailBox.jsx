import PropTypes from 'prop-types';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { useEffect, useRef, useState } from 'react';
import { v4 as uuidv4 } from 'uuid';

MailBox.propTypes = {
    userEmail: PropTypes.string,
};

function MailBox() {
    const urlParams = useParams();

    const [to, setTo] = useState('');
    const [body, setBody] = useState('');
    const [subject, setSubject] = useState('');
    const [priority, setPriority] = useState(2);
    const [attachment, setAttachment] = useState(null);

    // Generate a unique ID if not provided in the URL params
    const id = useRef(urlParams.id || uuidv4()).current;

    useEffect(() => {
        async function fetchMails() {
            if (urlParams.id !== undefined) {
                try {
                    const response = await axios.get(`http://localhost:8080/api/users/getDraft`, { params: { id: urlParams.id } });
                    const m = response.data;
                    setTo(m.receivers.join(' '));
                    setBody(m.body);
                    setSubject(m.subject);
                    setPriority(m.priority);
                } catch (error) {
                    console.error('Error fetching emails:', error);
                }
            }
        }

        fetchMails();
    }, [urlParams.id]);

    const handleEntryChange = (e) => {
        const { id, value } = e.target;
        switch (id) {
            case 'to':
                setTo(value);
                break;
            case 'subject':
                setSubject(value);
                break;
            case 'body':
                setBody(value);
                break;
            case 'pri':
                setPriority(value);
                break;
            default:
                break;
        }
        createDraft();
    };

    const createDraft = async () => {
        const param = {
            sender: urlParams.user,
            receivers: to.split(' '),
            subject: subject,
            body: body,
            priority: priority,
            attachments : [attachment]
        };

        try {
            await axios.post(`http://localhost:8080/api/users/createDraft?id=${id}`, param);
        } catch (error) {
            console.error('Error posting data:', error);
        }
    };

    const sendMail = async (e) => {

        e.preventDefault()

        const param = {
            sender: urlParams.user,
            receivers: to.split(' '),
            subject: subject,
            body: body,
            priority: priority,
            attachments : [attachment]

        };
            console.log("heeree")
        console.log(param)
        try {
            await axios.post(`http://localhost:8080/api/users/deleteDraft/${id}/${urlParams.user}`);
            await axios.post('http://localhost:8080/api/users/send', param);
        } catch (error) {
            console.error('Error posting data:', error);
        }
    };

    function test(e){

        console.log("hello");

        /////////////////////////////////////

                const file = e.target.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.onload = () => {
                        // File content as an ArrayBuffer
                        const arrayBuffer = reader.result;
                        const byteArray = new Uint8Array(arrayBuffer);
                        const attach = {
                            id : 12,
                            fileName: e.target.files[0].name,
                            fileType: e.target.files[0].type,
                            fileContent :Array.from(byteArray)
                        }
                        setAttachment(attach);
                        console.log(attachment);
                    };
                    // Read file as ArrayBuffer
                    reader.readAsArrayBuffer(file);
                }


        ////////////////////////////////////
    }

    return (
        <div className="d-flex flex-column mb-3 mail-box">
            <form className="form-control" >
                <table className="table table-striped">
                    <tbody>
                    <tr>
                        <td><label htmlFor="sender">From: </label></td>
                        <td><h6>{urlParams.user}</h6></td>
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
                                value={to}
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
                                value={subject}
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
                                    value={body}
                                    onChange={handleEntryChange}
                                />
                        </td>
                    </tr>
                    <tr>
                        <td><label htmlFor="priority">Priority: </label></td>
                        <td><input id="pri" className="input input-number" type="number" min="0" max="5" value={priority} onChange={handleEntryChange} /></td>
                    </tr>
                    <tr>
                        <td><label htmlFor="attachment">Attachment: </label></td>
                        <td><input className="input input-file" type="file" onChange={test}/></td>
                    </tr>
                    </tbody>
                </table>
                <button
                    className="btn btn-primary btn-lg btn-block position-relative"
                    style={{ marginLeft: '87%', marginRight: '20px', marginTop: '-9px', marginBottom: '10px' }}
                    type="submit"
                    onClick={sendMail}
                    disabled={!to.trim() || !body.trim() || !subject.trim()}
                >
                    Submit
                </button>
            </form>
        </div>
    );
}

export default MailBox;
