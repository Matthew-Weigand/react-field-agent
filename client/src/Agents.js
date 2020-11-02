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

  deleteAgentHandler = (agentId) => {
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

  editAgentHandler = (agentId) => {
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
        <h2 class="text-center">Agents</h2>
        <table className="table table-hover table-sm">
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
                  <button class="btn btn-sm btn-dark" type="button" onClick={() => this.editAgentHandler(agent.agentId)}>
                    Update Agent
                    </button>
                  <button class="btn btn-sm btn-outline-danger" type="button" onClick={() => this.deleteAgentHandler(agent.agentId)}>
                    Delete Agent
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        <br /><br />

        {mode === "Add" && (
          <form onSubmit={this.addSubmitHandler}>
            <h5>Add an Agent</h5>
            <div className="form-group row">
              <div class="col-6">
                <label>First Name: </label>
                <input type="text" className="form-control" name="firstName" value={this.state.firstName}
                  onChange={this.changeHandler} placeholder="Enter first name" />< br />
              </div>
              <div class="col-6">
                <label>Middle Name: </label>
                <input type="text" className="form-control" name="middleName" value={this.state.middleName}
                  onChange={this.changeHandler} placeholder="Enter middle name" />< br />
              </div>
              <div class="col-6">
                <label>Last Name: </label>
                <input type="text" className="form-control" name="lastName" value={this.state.lastName}
                  onChange={this.changeHandler} placeholder="Enter last name" />< br />
              </div>
              <div class="col-6">
                <label>D.O.B: </label>
                <input type="date" className="form-control" name="dob" value={this.state.dob}
                  onChange={this.changeHandler} />< br />
              </div>
              <div class="col-6">
                <label>Height (in Inches): </label>
                <input type="text" className="form-control" name="heightInInches" value={this.state.heightInInches}
                  onChange={this.changeHandler} placeholder="Enter height" />< br />
              </div>

            </div>
            <div>
              <button type="submit" class="btn btn-block btn-primary">Add Agent</button>
            </div>
          </form>
        )}

        {mode === "Edit" && (
          <form onSubmit={this.editSubmitHandler}>
            <h5>Update an Agent</h5>
            <br />
            <div className="form-group row">
              <div class="col-6">
                <label>First Name: </label>
                <input type="text" className="form-control" name="firstName" value={this.state.firstName}
                  onChange={this.changeHandler} placeholder="Enter first name" />< br />
              </div>
              <div class="col-6">
                <label>Middle Name: </label>
                <input type="text" className="form-control" name="middleName" value={this.state.middleName}
                  onChange={this.changeHandler} placeholder="Enter middle name" />< br />
              </div>
              <div class="col-6">
                <label>Last Name: </label>
                <input type="text" className="form-control" name="lastName" value={this.state.lastName}
                  onChange={this.changeHandler} placeholder="Enter last name" />< br />
              </div>
              <div class="col-6">
                <label>D.O.B: </label>
                <input className="form-control" type="date" name="dob" value={this.state.dob}
                  onChange={this.changeHandler} />< br />
              </div>
              <div class="col-6">
                <label>Height (in Inches): </label>
                <input type="text" className="form-control" name="heightInInches" value={this.state.heightInInches}
                  onChange={this.changeHandler} placeholder="Enter height" />< br />
              </div>
            </div>
            <div>
              <button className="btn btn-block btn-primary" type="submit">Update Agent </button><br />
            </div>
            <div>
              <button className="btn btn-block btn-dark" onClick={this.cancelAgentUpdate} type="button">Cancel</button>
            </div>
          </form>
        )}
      </>
    );
  }
}
export default Agents;
