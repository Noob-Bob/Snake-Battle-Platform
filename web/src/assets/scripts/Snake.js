/**
 * create a snake - a set of cells
 */
import { AcGameObject } from "./AcGameObject";
import { Cell } from "./Cell";

export class Snake extends AcGameObject {
    constructor(info, gamemap) {
        super();

        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;

        this.cells = [new Cell(info.r, info.c)]; // body of the snake, cells[0] is the head
        this.next_cell = null; // destination of the next step

        this.speed = 5; // 5 cells per second
        this.direction = -1; // -1: no direction, 0: up, 1: right, 2: down, 3: left
        this.status = "idle"; // idle: static, move: moving, die: dead

        this.dr = [-1, 0, 1, 0]; // delta in row
        this.dc = [0, 1, 0, -1]; // delta in col

        this.step = 0; // number of game rounds, used to check whether the length should change
        this.eps = 1e-2; // erors allowed

        this.eye_direction = 0;
        if (this.id === 1) this.eye_direction = 2; // bottom-left: up, top-right: down

        this.eye_dx = [
            [-1, 1],
            [1, 1],
            [1, -1],
            [-1, -1],
        ];
        this.eye_dy = [
            [-1, -1],
            [-1, 1],
            [1, 1],
            [1, -1],
        ]
    }

    start() {

    }

    set_direction(d) {
        this.direction = d;
    }

    check_tail_increasing() { // check whether the length of the snake increases
        if (this.step <= 10) return true;
        if (this.step % 3 === 1) return true;
        return false;
    }
    /**
     * change the status into "move"
     */
    next_step() { 
        const d = this.direction;
        // next_cell is the target cell for the new snake head
        // but currently not added into the body
        this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);
        this.eye_direction = d;
        this.direction = -1;
        this.status = "move";
        this.step ++;

        const k = this.cells.length;
        // effect: copy the orginal body and add all the cells back into the body array
        // keep the original head unchanged
        for (let i = k; i > 0; i --) {
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1])); // deep copy
        }
    }
    /**
     * render the animation, let the snake move on the canvas
     * 
     * Note that update_move() is called within update() function, meaning that it will be called
     * about 60 times per second
     */
    update_move() {

        const dx = this.next_cell.x - this.cells[0].x; // delta x for the head
        const dy = this.next_cell.y - this.cells[0].y; // delta y for the head
        const distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < this.eps) { // reach the target
            this.cells[0] = this.next_cell; // add a new head
            this.next_cell = null;
            this.status = "idle"; // stop moving

            if (!this.check_tail_increasing()) {
                this.cells.pop();
            }
        } else {
            const move_distance = this.speed * this.timedelta / 1000; // distance per flash
            this.cells[0].x += move_distance * dx / distance;
            this.cells[0].y += move_distance * dy / distance;

            if (!this.check_tail_increasing()) {
                const k = this.cells.length;
                const tail = this.cells[k - 1], tail_target = this.cells[k - 2];
                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                tail.x += move_distance * tail_dx / distance;
                tail.y += move_distance * tail_dy / distance;
            }
        }

    }

    update() { // execute once per flash
        if (this.status === 'move') {
            this.update_move();
        }
        this.render();
    }

    render() {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;

        if (this.status === "die") {
            ctx.fillStyle = "white";
        }
        for (const cell of this.cells) {
            ctx.beginPath();
            ctx.arc(cell.x * L, cell.y * L, L / 2 * 0.8, 0, Math.PI * 2);
            ctx.fill();
        }

        // beautify the snake
        // using continuous shape to replace the original seperate circles
        for (let i = 1; i < this.cells.length; i ++) { 
            const a = this.cells[i - 1], b = this.cells[i];
            if (Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps) // same point
                continue;
            if (Math.abs(a.x - b.x) < this.eps) { // vertical
                ctx.fillRect((a.x - 0.4) * L, Math.min(a.y, b.y) * L, L * 0.8, Math.abs(a.y - b.y) * L);
            } else { // horizontal
                ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.4) * L, Math.abs(a.x - b.x) * L, L * 0.8);
            }
        }

        // render the eyes of the snake
        ctx.fillStyle = "black";
        for (let i = 0; i < 2; i ++) {
            const eye_x = (this.cells[0].x + this.eye_dx[this.eye_direction][i] * 0.15) * L;
            const eye_y = (this.cells[0].y + this.eye_dy[this.eye_direction][i] * 0.15) * L;
            ctx.beginPath();
            ctx.arc(eye_x, eye_y, L * 0.05, 0, Math.PI * 2);
            ctx.fill();
        }
    }
}