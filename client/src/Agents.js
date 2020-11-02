import React from "react";

class Agents extends React.Component {
  constructor() {
    super();
    this.state = {
      agents: [],
      id: 0,
      firstName: ``,
      middleName: ``,
      lastName: ``,
      dob: ``,
      heightInInches: ``,
      mode: "Add",
    };
  }

  getAgents = () => {
    fetch("http://localhost:8080/api/agent")
      .then((response) => response.json())
      .then((data) => {
        this.setState({
          agents: data,
          id: 0,
          firstName: ``,
          middleName: ``,
          lastName: ``,
          dob: ``,
          heightInInches: ``,
          mode: `Add`,
        });
      });
  };

  componentDidMount() {
    this.getAgents();
  }

  changeHandler = (event) => {
    const target = event.target;
    const value = target.value;
    const firstName = target.name;
    const lastName = target.name;
    const middleName = target.name;
    const dob = target.name;
    const heightInInches = target.name;

    this.setState({
      [firstName]: value,
      [lastName]: value,
      [middleName]: value,
      [dob]: value,
      [heightInInches]: value,
    });
  };

  addSubmitHandler = (event) => {
    event.preventDefault();

    fetch(`http://localhost:8080/api/agent`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        firstName: this.state.firstName,
        middleName: this.state.middleName,
        lastName: this.state.lastName,
        dob: this.state.dob,
        heightInInches: this.state.heightInInches,
      }),
    }).then((response) => {
      if (response.status === 201) {
        console.log("Success!");
        response.json().then((data) => console.log(data));
        this.getAgents();
      } else if (response.status === 400) {
        console.log("Errors!");
        response.json().then((data) => console.log(data));
      } else {
        console.log("oops...");
      }
    });
  };

  deleteToDoHandler = (agentId) => {
    console.log("Delete to do  " + agentId);

    fetch(`http://localhost:8080/api/agent/${agentId}`, {
      method: "DELETE",
    }).then((response) => {
      if (response.status === 200) {
        console.log("Success");
        this.getAgents();
      } else {
        console.log("Delete failed: " + response);
      }
    });
  };

  editToDoHandler = (agentId) => {
    console.log("Edit agent " + agentId);

    fetch(`http://localhost:8080/api/agent/${agentId}`)
      .then((response) => response.json())
      .then(
        ({ agentId, firstName, middleName, lastName, dob, heightInInches }) => {
          this.setState({
            agentId,
            firstName,
            middleName,
            lastName,
            dob,
            heightInInches,
            mode: "Edit",
          });
        }
      );
  };

  editSubmitHandler = (event) => {
    event.preventDefault();

    const {
      agentId,
      firstName,
      middleName,
      lastName,
      dob,
      heightInInches,
    } = this.state;

    fetch(`http://localhost:8080/api/agent/${agentId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        agentId,
        firstName,
        middleName,
        lastName,
        dob,
        heightInInches,
      }),
    }).then((response) => {
      if (response.status === 204) {
        console.log("Success!");
        this.getAgents();
      } else if (response.status === 400) {
        console.log("Errors!");
        response.json().then((data) => console.log(data));
      } else {
        console.log("oops...");
      }
    });
  };

  cancelAgentUpdate = () => {
    this.setState({
      id: 0,
      firstName: ``,
      middleName: ``,
      lastName: ``,
      dob: ``,
      heightInInches: ``,
      mode: "Add",
    });
  };

  render() {
    const { mode } = this.state;
    return (
      <>
        <h2>Agents</h2>

        <table className="table table-hover">
          <thead className="thead-dark">
            <tr>
              <th scope="col">Agent Id</th>
              <th scope="col">First Name</th>
              <th scope="col">Last Name</th>
              <th scope="col">D.O.B</th>
              <th scope="col">Height in Inches</th>
              <th scope="col">Manage Agents</th>
            </tr>
          </thead>
          <tbody>
            {this.state.agents.map((agent) => (
              <tr key={agent.agentId}>
                <td>{agent.agentId}</td>
                <td>{agent.firstName}</td>
                <td>{agent.lastName}</td>
                <td>{agent.dob}</td>
                <td>{agent.heightInInches}</td>
                <td>
                  <button
                    type="button"
                    onClick={() => this.editToDoHandler(agent.agentId)}
                  >
                    Edit Agent
                  </button>
                  <button
                    type="button"
                    onClick={() => this.deleteToDoHandler(agent.agentId)}
                  >
                    Delete Agent
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        <br />
        <br />
        <h5>Add an Agent</h5>
        {mode === "Add" && (
          <form onSubmit={this.addSubmitHandler}>
            <label>
              First Name:
              <input
                name="firstName"
                value={this.state.firstName}
                onChange={this.changeHandler}
                type="text"
              />
            </label>
            <br />
            <label>
              Middle Name:
              <input
                name="middleName"
                value={this.state.middleName}
                onChange={this.changeHandler}
                type="text"
              />
            </label>
            <br />
            <label>
              Last Name:
              <input
                name="lastName"
                value={this.state.lastName}
                onChange={this.changeHandler}
                type="text"
              />
            </label>
            <br />
            <label>
              DOB:
              <input
                name="dob"
                value={this.state.dob}
                onChange={this.changeHandler}
                type="date"
              />
            </label>
            <br />
            <label>
              Height (in inches):
              <input
                name="heightInInches"
                value={this.state.heightInInches}
                onChange={this.changeHandler}
                type="text"
              />
            </label>
            <br />
            <button type="submit">Add Agent</button>
          </form>
        )}

        {mode === "Edit" && (
          <form onSubmit={this.editSubmitHandler}>
              <div className="form-group">
              <label>
              First Name:
              <input
                name="firstName"
                value={this.state.firstName}
                onChange={this.changeHandler}
                type="text"
              />
            </label>
            <br />
            <label>
              Middle Name:
              <input
                name="middleName"
                value={this.state.middleName}
                onChange={this.changeHandler}
                type="text"
              />
            </label>
            <br />
            <label>
              Last Name:
              <input
                name="lastName"
                value={this.state.lastName}
                onChange={this.changeHandler}
                type="text"
              />
            </label>
            <br />
            <label>
              DOB:
              <input
                name="dob"
                value={this.state.dob}
                onChange={this.changeHandler}
                type="date"
              />
            </label>
            <br />
            <label>
              Height (in inches):
              <input
                name="heightInInches"
                value={this.state.heightInInches}
                onChange={this.changeHandler}
                type="text"
              />
            </label>
            <br />
            <button type="submit">Update Agent</button>
            <button onClick={this.cancelAgentUpdate} type="button">
              Cancel Update
            </button>
              </div>
           
          </form>
        )}


        
      </>
    );
  }
}

export default Agents;
