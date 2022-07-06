(load 0_Main.clp)
(load 1_Env.clp)
(load maps/map_1_know_5.clp)
(load main_agent.clp)
(load main_control.clp)
(load after_no_know.clp)
(load know_double_middle.clp)
(load know_two.clp)
(load know_one.clp)
(load know_one_indecision.clp)
(load know_middle.clp)
(load no_know.clp)
(reset)

(run)

(and
(dribble-on file_di_log/outputAGENT.clp)
(facts AGENT)
(dribble-off)
)


