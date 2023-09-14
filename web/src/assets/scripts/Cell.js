/**
 * A cell is a unit of a snake
 * 
 * we need some conversion from r-c coordinate to canvas x - y coordinate
 * note that cell is a circle, its center should have a offset
 * 
 */
export class Cell {
    constructor(r, c) {
        this.r = r;
        this.c = c;
        this.x = c + 0.5;
        this.y = r + 0.5;
    }
}